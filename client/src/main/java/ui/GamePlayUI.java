package ui;


import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import model.GameData;
import websocket.commands.*;
import websocket.messages.*;
import java.util.Scanner;
import javax.websocket.*;
import java.net.URI;

public class GamePlayUI extends Endpoint {

    private final Integer gameID;
    private final String teamColor;
    public Session session;
    private final String authToken;
    private GameData gameData;
    private boolean loadedGame;

    public GamePlayUI(Integer gameNum, String teamColor, String token) {
        gameID = gameNum;
        this.teamColor = teamColor;
        authToken = token;
        loadedGame = false;
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }


    public boolean tryConnectToGame() {
        try {
            URI uri = new URI("ws://localhost:8080/ws");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, uri);
            String json = new Gson().toJson(new ConnectCommand(authToken, gameID));
            send(json);
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    switch(serverMessage.getServerMessageType()) {
                        case NOTIFICATION -> {
                            NotificationMessage notification = new Gson().fromJson(message, NotificationMessage.class);
                            System.out.printf(EscapeSequences.SET_TEXT_COLOR_YELLOW);
                            System.out.println(notification.getMessage());
                            System.out.printf(EscapeSequences.RESET_TEXT_COLOR);
                            editGameData(notification.getMessage());
                        }
                        case ERROR -> {
                            ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                            System.out.printf(EscapeSequences.SET_TEXT_COLOR_RED);
                            System.out.println(errorMessage.getErrorMessage());
                            System.out.printf(EscapeSequences.RESET_TEXT_COLOR);
                        }
                        case LOAD_GAME -> {
                            OnGameLoadMessage loadMessage = new Gson().fromJson(message, OnGameLoadMessage.class);
                            gameData = loadMessage.getGame();
                            printBoards();
                            if(teamColor.equals("SPECTATOR"))
                                listObserveOptions();
                            else
                                listGameOptions();
                        }
                    }
                }
            });
        } catch(Exception ex) {
            return false;
        }
        return true;
    }

    public void  inGame(boolean isObserver) {
        System.out.println("\nSuccessfully joined game.");
        Scanner scan = new Scanner(System.in);
        String input;
        boolean stop = false;

        //printBoards();

        if(!isObserver) {
            while (true) {
                input = scan.nextLine();

                if (input.equals("1") || input.equalsIgnoreCase("help")) {
                    listGameExplanations();
                } else if (input.equals("2") || input.equalsIgnoreCase("redraw chess board") || input.equals("redraw")) {
                    printBoards();
                } else if (input.equals("3") || input.equalsIgnoreCase("leave game") || input.equals("leave")) {
                    try {
                        String json = new Gson().toJson(new LeaveGameCommand(authToken, gameID));
                        send(json);
                    } catch(Exception ex) {
                        System.out.println("Leave Game Message Sending Error");
                    }
                    break;
                } else if (input.equals("4") || input.equalsIgnoreCase("make move") || input.equalsIgnoreCase("move")) {
                    makeMove();
                } else if (input.equals("5") || input.equalsIgnoreCase("resign")) {
                    try {
                        String json = new Gson().toJson(new ResignCommand(authToken, gameID));
                        send(json);
                    } catch(Exception ex) {
                        System.out.println("Resign Message Sending Error");
                    }
                } else if (input.equals("6") || input.equalsIgnoreCase("highlight legal moves") || input.equalsIgnoreCase("highlight")) {
                    printValidMoves();
                } else {
                    System.out.println("Invalid option, please enter 1 or help to see your explanations for your options.");
                }
                listGameOptions();
                System.out.println("\nPlease input your selection: ");
            }
        } else {
            while(!stop) {
                listObserveOptions();
                System.out.println("\nPlease input your selection: ");
                input = scan.nextLine();

                if(input.equals("1") || input.equalsIgnoreCase("help")) {
                    listObserveExplanations();
                } else if(input.equals("2") || input.equalsIgnoreCase("quit")) {
                    try {
                        String json = new Gson().toJson(new LeaveGameCommand(authToken, gameID));
                        send(json);
                    } catch(Exception ex) {
                        System.out.println("Leave Game Message Sending Error");
                    }
                    stop = true;
                } else if(input.equals("3") || input.equalsIgnoreCase("redraw chess board") || input.equalsIgnoreCase("redraw")) {
                    printBoards();
                } else if(input.equals("4") || input.equalsIgnoreCase("highlight") || input.equalsIgnoreCase("highlight legal moves")) {
                    printValidMoves();
                } else {
                    System.out.println("Invalid option, please enter 1 or help to see your explanations for your options.");
                }
            }
        }
    }

    static void listObserveOptions() {
        System.out.println("\n 1. Help");
        System.out.println(" 2. Quit");
        System.out.println(" 3. Redraw Chess Board");
        System.out.println(" 4. Highlight legal moves");
    }

    static void listObserveExplanations() {
        System.out.println("\nInput 1 or help to show this list again");
        System.out.println("Input 2 or quit to leave the game");
        System.out.println("Input 3 or redraw chess board to redraw the chess board.\n" +
                "  (Best after being notified that a move has been made.)");
        System.out.println("Input 4 or highlight legal moves. You can input a position where a piece is, like d6\n" +
                "  (This will highlight all possible moves from the starting position.)");
    }

    static void listGameOptions() {
        System.out.println("\n 1. Help");
        System.out.println(" 2. Redraw Chess Board");
        System.out.println(" 3. Leave Game");
        System.out.println(" 4. Make Move");
        System.out.println(" 5. Resign");
        System.out.println(" 6. Highlight Legal Moves");
    }

    static void listGameExplanations() {
        System.out.println("\nInput 1 or help to show this list again");
        System.out.println("Input 2 or redraw chess board to redraw the chess board.\n" +
                "  (Best after being notified that a move has been made.)");
        System.out.println("Input 3 or leave game to leave the game and let others take your place in the game");
        System.out.println("Input 4 or make move to make a move when it is your turn. Example move: c7 c8 Queen\n" +
                "  (c7 is the starting position, c8 is the end position, and Queen is the pawn promotion)");
        System.out.println("Input 5 or resign to resign the game and have your opponent win.");
        System.out.println("Input 6 or highlight legal moves. You can input a position where a piece is, like d6\n" +
                "  (This will highlight all possible moves from the starting position.)");
    }

    public void printBoards() {
        GameBoardUI ui = new GameBoardUI(gameData.game().getBoard().getBoard());
        printInfo();
        if(teamColor.equalsIgnoreCase("BLACK")) {
            ui.printBlackSideBoard(false);
        } else {
            ui.printWhiteSideBoard(false);
        }
    }

    public void printValidMoves() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please input the position of the piece you would like to see the valid moves of");
        String startPos = scan.nextLine();

        int startCol;
        int startRow;

        try {
            startRow = Integer.parseInt(String.valueOf(startPos.charAt(1)));
        } catch(Exception ex) {
            System.out.println("Invalid move");
            return;
        }

        if(charToInt(startPos.charAt(0)) == -1) {
            System.out.println("Invalid move");
            return;
        } else {
            startCol = charToInt(startPos.charAt(0));
        }

        GameBoardUI ui = new GameBoardUI(gameData.game().getBoard().getBoard());
        printInfo();
        ui.setValidMoves(gameData.game().validMoves(new ChessPosition(startRow, startCol)));
        ui.setSelectedPiece(new ChessPosition(startRow, startCol));
        if(teamColor.equalsIgnoreCase("BLACK")) {
            ui.printBlackSideBoard(true);
        } else {
            ui.printWhiteSideBoard(true);
        }
    }

    public void printInfo() {
        System.out.println("\nGame number: " + gameData.gameID() + ", Game name: " + gameData.gameName());
        System.out.println("White username: " + ((gameData.whiteUsername() == null) ? "<Empty>" : gameData.whiteUsername()) + ", Black username: " + ((gameData.blackUsername() == null) ? "<Empty>" : gameData.blackUsername()));
        System.out.println("Current game condition: " + ((gameData.game().isGameOver()) ? "Finished" : "Ongoing"));
    }

    public void editGameData(String message) {
        String username;
        GameData newGame;
        if(message.endsWith("joined the game as White")) {
            username = getUsername(message, 25);
            newGame = new GameData(gameData.gameID(), username, gameData.blackUsername(), gameData.gameName(),gameData.game());
        } else if(message.endsWith("joined the game as Black")) {
            username = getUsername(message, 25);
            newGame = new GameData(gameData.gameID(), gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
        } else if(message.endsWith(" has left the game")) {
            username = getUsername(message, 18);
            if(gameData.whiteUsername().equals(username))
                newGame = new GameData(gameData.gameID(), null, gameData.blackUsername(), gameData.gameName(), gameData.game());
            else if(gameData.blackUsername().equals(username))
                newGame = new GameData(gameData.gameID(), gameData.whiteUsername(), null, gameData.gameName(), gameData.game());
            else
                return;
        } else if(message.endsWith("has resigned the game")) {
            ChessGame resignGame = new ChessGame();
            resignGame.setBoard(gameData.game().getBoard());
            resignGame.setGameOver(true);
            newGame = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), resignGame);
        }
        else
            return;
        gameData = newGame;
    }

    public String getUsername(String message, int cutOffAmount) {
        return message.substring(0,message.length()-cutOffAmount);
    }

    public void makeMove() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please input starting position");
        String startPos = scan.nextLine();
        System.out.println("Please input end position");
        String endPos = scan.nextLine();

        int startRow;
        int startCol;
        int endRow;
        int endCol;

        try {
            startRow = Integer.parseInt(String.valueOf(startPos.charAt(1)));
            endRow = Integer.parseInt(String.valueOf(endPos.charAt(1)));
        } catch(Exception ex) {
            System.out.println("Invalid move");
            return;
        }

        if(charToInt(startPos.charAt(0)) == -1) {
            System.out.println("Invalid move");
            return;
        } else {
            startCol = charToInt(startPos.charAt(0));
        }

        if(charToInt(endPos.charAt(0)) == -1) {
            System.out.println("Invalid move");
            return;
        } else
            endCol = charToInt(endPos.charAt(0));

        String promotionInput;
        ChessPiece.PieceType pawnPromotion;
        if((gameData.game().getBoard().getPiece(new ChessPosition(startRow, startCol)) != null) && (gameData.game().getBoard().getPiece(new ChessPosition(startRow, startCol)).getPieceType() == ChessPiece.PieceType.PAWN) && (endRow == 8)) {
            System.out.println("Please input pawn promotion (Queen, Rook, Bishop, or Knight)");
            promotionInput = scan.nextLine();
            if(promotionInput.equalsIgnoreCase("QUEEN"))
                pawnPromotion = ChessPiece.PieceType.QUEEN;
            else if(promotionInput.equalsIgnoreCase("ROOK"))
                pawnPromotion = ChessPiece.PieceType.ROOK;
            else if(promotionInput.equalsIgnoreCase("BISHOP"))
                pawnPromotion = ChessPiece.PieceType.BISHOP;
            else if(promotionInput.equalsIgnoreCase("KNIGHT"))
                pawnPromotion = ChessPiece.PieceType.KNIGHT;
            else {
                System.out.println("Invalid move");
                return;
            }
        } else
            pawnPromotion = null;

        try {
            ChessMove move = new ChessMove(new ChessPosition(startRow, startCol), new ChessPosition(endRow, endCol), pawnPromotion);
            String json = new Gson().toJson(new MakeMoveCommand(authToken, gameID, move));
            send(json);
        } catch(Exception ex) {
            System.out.println("Make Move Message Sending Error");
        }
    }

    public int charToInt(Character col) {
        switch (col) {
            case 'a' -> { return 1; }
            case 'b' -> { return 2; }
            case 'c' -> { return 3; }
            case 'd' -> { return 4; }
            case 'e' -> { return 5; }
            case 'f' -> { return 6; }
            case 'g' -> { return 7; }
            case 'h' -> { return 8; }
            default -> {
                System.out.println("Invalid move");
                return -1;
            }
        }
    }
}