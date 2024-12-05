package ui;

import chess.*;
import com.google.gson.Gson;
import models.GameData;
import websocket.commands.*;
import websocket.messages.*;

import javax.websocket.*;
import java.net.URI;
import java.util.Scanner;

public class Game extends Endpoint {

    private final Integer gameID;
    private final String teamColor;
    private final String authToken;
    private Session session;
    private GameData gameData;
    private boolean loadedGame;

    public Game(Integer gameNum, String teamColor, String token) {
        this.gameID = gameNum;
        this.teamColor = teamColor;
        this.authToken = token;
        this.loadedGame = false;
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        // Called when a new WebSocket connection is opened
    }

    public boolean tryConnectToGame() {
        try {
            URI uri = new URI("ws://localhost:8080/ws");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, uri);
            String json = new Gson().toJson(new Connect(authToken, gameID));
            send(json);
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    switch(serverMessage.getServerMessageType()) {
                        case NOTIFICATION -> {
                            Notification notification = new Gson().fromJson(message, Notification.class);
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
                            LoadGame loadMessage = new Gson().fromJson(message, LoadGame.class);
                            gameData = loadMessage.getGame();
                            printBoards();
                            if(teamColor.equalsIgnoreCase("SPECTATOR"))
                                listObserveOptions();
                            else
                                listGameOptions();

                            // Start inGame() in a new thread
                            new Thread(() -> {
                                inGame(teamColor.equalsIgnoreCase("SPECTATOR"));
                            }).start();
                        }
                    }
                }
            });
        } catch(Exception ex) {
            return false;
        }
        return true;
    }

    public void inGame(boolean isObserver) {
        System.out.println("\nSuccessfully joined game.");
        Scanner scanner = new Scanner(System.in);
        String input;

        if (!isObserver) {
            while (true) {
                System.out.println("\nPlease input your selection: ");
                input = scanner.nextLine();

                if (input.equals("1") || input.equalsIgnoreCase("help")) {
                    listGameExplanations();
                } else if (input.equals("2") || input.equalsIgnoreCase("redraw") || input.equalsIgnoreCase("redraw chess board")) {
                    printBoards();
                } else if (input.equals("3") || input.equalsIgnoreCase("leave") || input.equalsIgnoreCase("leave game")) {
                    try {
                        String json = new Gson().toJson(new Leave(authToken, gameID));
                        send(json);
                    } catch (Exception ex) {
                        System.out.println("Error sending leave game message.");
                    }
                    break;
                } else if (input.equals("4") || input.equalsIgnoreCase("move") || input.equalsIgnoreCase("make move")) {
                    makeMove();
                } else if (input.equals("5") || input.equalsIgnoreCase("resign")) {
                    try {
                        String json = new Gson().toJson(new Resign(authToken, gameID));
                        send(json);
                    } catch (Exception ex) {
                        System.out.println("Error sending resign message.");
                    }
                } else if (input.equals("6") || input.equalsIgnoreCase("highlight") || input.equalsIgnoreCase("highlight legal moves")) {
                    printValidMoves();
                } else {
                    System.out.println("Invalid option. Please enter 1 or 'help' to see available options.");
                }
                listGameOptions();
            }
        } else {
            boolean stop = false;
            while (!stop) {
                System.out.println("\nPlease input your selection: ");
                listObserveOptions();
                input = scanner.nextLine();

                if (input.equals("1") || input.equalsIgnoreCase("help")) {
                    listObserveExplanations();
                } else if (input.equals("2") || input.equalsIgnoreCase("quit")) {
                    try {
                        String json = new Gson().toJson(new Leave(authToken, gameID));
                        send(json);
                    } catch (Exception ex) {
                        System.out.println("Error sending leave game message.");
                    }
                    stop = true;
                } else if (input.equals("3") || input.equalsIgnoreCase("redraw") || input.equalsIgnoreCase("redraw chess board")) {
                    printBoards();
                } else if (input.equals("4") || input.equalsIgnoreCase("highlight") || input.equalsIgnoreCase("highlight legal moves")) {
                    printValidMoves();
                } else {
                    System.out.println("Invalid option. Please enter 1 or 'help' to see available options.");
                }
            }
        }
    }

    private static void listObserveOptions() {
        System.out.println("\n 1. Help");
        System.out.println(" 2. Quit");
        System.out.println(" 3. Redraw Chess Board");
        System.out.println(" 4. Highlight Legal Moves");
    }

    private static void listObserveExplanations() {
        System.out.println("\n1. Help - Show this list again.");
        System.out.println("2. Quit - Leave the game.");
        System.out.println("3. Redraw Chess Board - Redraw the chess board (useful after a move has been made).");
        System.out.println("4. Highlight Legal Moves - Input a position (e.g., d6) to highlight possible moves from that position.");
    }

    private static void listGameOptions() {
        System.out.println("\n 1. Help");
        System.out.println(" 2. Redraw Chess Board");
        System.out.println(" 3. Leave Game");
        System.out.println(" 4. Make Move");
        System.out.println(" 5. Resign");
        System.out.println(" 6. Highlight Legal Moves");
    }

    private static void listGameExplanations() {
        System.out.println("\n1. Help - Show this list again.");
        System.out.println("2. Redraw Chess Board - Redraw the chess board (useful after a move has been made).");
        System.out.println("3. Leave Game - Leave the game and allow others to take your place.");
        System.out.println("4. Make Move - Make a move when it's your turn (e.g., 'c7 c8 Queen' for a pawn promotion).");
        System.out.println("5. Resign - Resign the game, resulting in a win for your opponent.");
        System.out.println("6. Highlight Legal Moves - Input a position (e.g., d6) to highlight possible moves from that position.");
    }

    public void printBoards() {
        BoardUI ui = new BoardUI(gameData.game().getBoard().getBoard());
        printInfo();
        boolean isWhite = !teamColor.equalsIgnoreCase("BLACK");
        ui.printBoard(isWhite);
    }

    public void printValidMoves() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please input the position of the piece you would like to see the valid moves of:");
        String startPos = scanner.nextLine();

        int startCol = charToInt(startPos.charAt(0));
        if (startCol == -1) {
            System.out.println("Invalid position");
            return;
        }

        int startRow;
        try {
            startRow = Integer.parseInt(String.valueOf(startPos.charAt(1)));
        } catch (Exception ex) {
            System.out.println("Invalid position");
            return;
        }

        BoardUI ui = new BoardUI(gameData.game().getBoard().getBoard());
        printInfo();
        ChessPosition position = new ChessPosition(startRow, startCol);
        ui.setValidMoves(gameData.game().validMoves(position));
        ui.setSelectedPiece(position);

        boolean isWhite = !teamColor.equalsIgnoreCase("BLACK");
        ui.printBoard(isWhite);
    }

    public void printInfo() {
        System.out.println("\nGame number: " + gameData.gameID() + ", Game name: " + gameData.gameName());
        String whiteUsername = gameData.whiteUsername() == null ? "<Empty>" : gameData.whiteUsername();
        String blackUsername = gameData.blackUsername() == null ? "<Empty>" : gameData.blackUsername();
        System.out.println("White username: " + whiteUsername + ", Black username: " + blackUsername);
        String gameCondition = gameData.game().isGameOver() ? "Finished" : "Ongoing";
        System.out.println("Current game condition: " + gameCondition);
    }

    public void editGameData(String message) {
        String username;
        GameData newGameData;

        if (message.endsWith("joined the game as White")) {
            username = extractUsername(message, 25);
            newGameData = new GameData(gameData.gameID(), username, gameData.blackUsername(), gameData.gameName(), gameData.game());
        } else if (message.endsWith("joined the game as Black")) {
            username = extractUsername(message, 25);
            newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
        } else if (message.endsWith(" has left the game")) {
            username = extractUsername(message, 18);
            if (username.equals(gameData.whiteUsername())) {
                newGameData = new GameData(gameData.gameID(), null, gameData.blackUsername(), gameData.gameName(), gameData.game());
            } else if (username.equals(gameData.blackUsername())) {
                newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), null, gameData.gameName(), gameData.game());
            } else {
                return;
            }
        } else if (message.endsWith("has resigned the game")) {
            ChessGame resignedGame = new ChessGame();
            resignedGame.setBoard(gameData.game().getBoard());
            resignedGame.setGameOver(true);
            newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), resignedGame);
        } else {
            return;
        }
        gameData = newGameData;
    }

    public String extractUsername(String message, int cutOffAmount) {
        return message.substring(0, message.length() - cutOffAmount);
    }

    public int charToInt(char columnChar) {
        return switch (Character.toLowerCase(columnChar)) {
            case 'a' -> 1;
            case 'b' -> 2;
            case 'c' -> 3;
            case 'd' -> 4;
            case 'e' -> 5;
            case 'f' -> 6;
            case 'g' -> 7;
            case 'h' -> 8;
            default -> -1;
        };
    }

    public void makeMove() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please input starting position:");
        String startPos = scanner.nextLine();

        System.out.println("Please input end position:");
        String endPos = scanner.nextLine();

        int startCol = charToInt(startPos.charAt(0));
        int endCol = charToInt(endPos.charAt(0));

        if (startCol == -1 || endCol == -1) {
            System.out.println("Invalid move");
            return;
        }

        int startRow, endRow;
        try {
            startRow = Integer.parseInt(String.valueOf(startPos.charAt(1)));
            endRow = Integer.parseInt(String.valueOf(endPos.charAt(1)));
        } catch (Exception ex) {
            System.out.println("Invalid move");
            return;
        }

        ChessPiece.PieceType pawnPromotion = null;
        ChessPiece piece = gameData.game().getBoard().getPiece(new ChessPosition(startRow, startCol));
        if (piece != null && piece.getPieceType() == ChessPiece.PieceType.PAWN && endRow == 8) {
            System.out.println("Please input pawn promotion (Queen, Rook, Bishop, or Knight):");
            String promotionInput = scanner.nextLine();
            pawnPromotion = switch (promotionInput.toLowerCase()) {
                case "queen" -> ChessPiece.PieceType.QUEEN;
                case "rook" -> ChessPiece.PieceType.ROOK;
                case "bishop" -> ChessPiece.PieceType.BISHOP;
                case "knight" -> ChessPiece.PieceType.KNIGHT;
                default -> {
                    System.out.println("Invalid promotion");
                    yield null;
                }
            };
            if (pawnPromotion == null) {
                return;
            }
        }

        try {
            ChessMove move = new ChessMove(
                    new ChessPosition(startRow, startCol),
                    new ChessPosition(endRow, endCol),
                    pawnPromotion
            );
            String json = new Gson().toJson(new MakeMove(authToken, gameID, move));
            send(json);
        } catch (Exception ex) {
            System.out.println("Error sending make move message.");
        }
    }
}
