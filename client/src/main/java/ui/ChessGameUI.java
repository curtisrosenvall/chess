package ui;

import chess.*;
import com.google.gson.Gson;
import facade.ServerFacade;
import models.GameData;
import result.*;
import java.util.Scanner;
import websocket.commands.*;
import websocket.messages.*;
import models.*;
import javax.websocket.*;
import java.util.concurrent.CountDownLatch;
import java.net.URI;


public class ChessGameUI extends Endpoint {

    private ServerFacade serverFacade;
    private String authToken;
    private Scanner scan;
    private Integer gameID;
    private String teamColor;
    private Session session;
    private GameData gameData;
    private boolean loadedGame;
    private CountDownLatch gameLoadedLatch;

    public ChessGameUI() {
        serverFacade = new ServerFacade(8080);
        scan = new Scanner(System.in);
        loadedGame = false;
    }

    public void playChess() {
        boolean loggedIn = false;
        String input = "start";

        while (true) {
            if (input.equalsIgnoreCase("quit") || input.equals("2")) {
                break;
            }

            if (input.equalsIgnoreCase("start")) {
                chessStart();
            }

            System.out.println(EscapeSequences.SET_TEXT_COLOR_GREEN + "\nFor [GAME_OPTIONS] press [1] " + EscapeSequences.RESET_TEXT_COLOR);
            System.out.println(EscapeSequences.SET_TEXT_COLOR_WHITE + "\nPlease input your selection: " + EscapeSequences.RESET_TEXT_COLOR);
            input = scan.nextLine().trim();

            if (input.equalsIgnoreCase("quit") || input.equals("2")) {
                break;
            } else if (input.equalsIgnoreCase("help") || input.equals("1")) {
                helpExplainOptions(loggedIn);
            } else if (!loggedIn) {
                switch (input.toLowerCase()) {
                    case "register":
                    case "3":
                        loggedIn = registerUser();
                        break;
                    case "login":
                    case "4":
                        loggedIn = loginUser();
                        break;
                    default:
                        invalidInput();
                        break;
                }
            } else {
                // User is logged in
                switch (input.toLowerCase()) {
                    case "logout":
                    case "3":
                        loggedIn = logoutUser();
                        break;
                    case "create game":
                    case "4":
                        createGame();
                        break;
                    case "list games":
                    case "5":
                        listGames();
                        break;
                    case "play game":
                    case "6":
                        joinGame();
                        break;
                    case "observe game":
                    case "7":
                        observeGame();
                        break;
                    default:
                        invalidInput();
                        break;
                }
            }
        }
    }

    private void chessStart() {
        System.out.println(EscapeSequences.SET_TEXT_BOLD + "\n** [WELCOME!] to Curt's Chess Game Server **" + EscapeSequences.RESET_TEXT_BOLD_FAINT);
    }

    private void helpExplainOptions(boolean loggedIn) {
        if (!loggedIn) {
            System.out.println("\nTo " + EscapeSequences.SET_TEXT_BOLD + EscapeSequences.SET_TEXT_COLOR_BLUE +
                    "[REGISTER_USER] press [3]" + EscapeSequences.RESET_TEXT_BOLD_FAINT + EscapeSequences.RESET_TEXT_COLOR + " or enter 'register'.");
            System.out.println("You will then be prompted to provide a username, password, and email address.");
            System.out.println("To " + EscapeSequences.SET_TEXT_BOLD + EscapeSequences.SET_TEXT_COLOR_YELLOW +
                    "[LOGIN] press [4]" + EscapeSequences.RESET_TEXT_BOLD_FAINT + EscapeSequences.RESET_TEXT_COLOR + " or enter 'login'.");
            System.out.println("Simply input your username and password when prompted.");
        } else {
            System.out.println(EscapeSequences.SET_TEXT_COLOR_GREEN + "[LOGGED_IN >>>]" + EscapeSequences.RESET_TEXT_COLOR);
            System.out.println("To " + EscapeSequences.SET_TEXT_COLOR_RED + "[LOGOUT]"
                    + EscapeSequences.RESET_TEXT_COLOR + ", press '3' or enter 'logout'.");
            System.out.println("To " + EscapeSequences.SET_TEXT_COLOR_YELLOW + "[CREATE_GAME]"
                    + EscapeSequences.RESET_TEXT_COLOR + ", press '4' or enter 'create game'. " +
                    "You will need to input a game name.");
            System.out.println("To " + EscapeSequences.SET_TEXT_COLOR_BLUE + "[LIST_GAME]"
                    + EscapeSequences.RESET_TEXT_COLOR + ", press '5' or enter 'list games'.");
            System.out.println("To " + EscapeSequences.SET_TEXT_COLOR_ORANGE + "[JOIN_GAME]"
                    + EscapeSequences.RESET_TEXT_COLOR + ", press '6' or enter 'join game'. " +
                    "You will need to input the game ID and your team color.");
            System.out.println("To " + EscapeSequences.SET_TEXT_COLOR_WHITE + "[OBSERVE_GAME]"
                    + EscapeSequences.RESET_TEXT_COLOR + ", press '7' or enter 'observe game'. " +
                    "You will need to input the game ID you want to watch.");
        }
        System.out.println("\nPress [2] to quit the game.");
    }

    private boolean registerUser() {
        System.out.println("\nPlease enter your [USERNAME]: ");
        String username = scan.nextLine();
        System.out.println("Please enter your [PASSWORD]: ");
        String password = scan.nextLine();
        System.out.println("Please enter your [EMAIL]: ");
        String email = scan.nextLine();
        RegisterRes result = serverFacade.registerUser(username, password, email);

        if (result.getAuthToken() == null) {
            System.out.println(result.getMessage());
            return false;
        } else {
            System.out.println("[REGISTERED_SUCCESS] " + username);
            authToken = result.getAuthToken();
            return true;
        }
    }

    private boolean loginUser() {
        System.out.println("\nPlease enter your [USERNAME]: ");
        String username = scan.nextLine();
        System.out.println("Please enter your [PASSWORD]: ");
        String password = scan.nextLine();
        LoginRes result = serverFacade.loginUser(username, password);

        if (result.getAuthToken() == null) {
            System.out.println(result.getMessage());
            return false;
        } else {
            System.out.println("[LOGGED_IN] as " + username);
            authToken = result.getAuthToken();
            return true;
        }
    }

    private boolean logoutUser() {
        LogoutRes result = serverFacade.logoutUser(authToken);
        if (result.getMessage() == null) {
            System.out.println("Successfully logged out.");
            System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "\n[LOGGED_OUT >>> ]" + EscapeSequences.RESET_TEXT_COLOR);
            authToken = null;
            return false;
        } else {
            System.out.println(result.getMessage());
            return true;
        }
    }

    private void createGame() {
        System.out.println("\nPlease enter the [GAME NAME] you would like to create: ");
        String gameName = scan.nextLine();
        CreateGameRes result = serverFacade.createGame(gameName, authToken);
        if (result.getGameId() == null) {
            System.out.println(result.getMessage());
        } else {
            System.out.println("Successfully created \"" + gameName + "\", can be found with [GAME_ID] " + result.getGameId());
        }
    }

    private void listGames() {
        ListGamesRes result = serverFacade.listGames(authToken);
        if (result.getGames() == null) {
            System.out.println(result.getMessage());
        } else {
            System.out.println("List of games: ");
            int gameNumber = 1;
            for (GameData game : result.getGames()) {
                System.out.println("[GAME_NUMBER]= " + gameNumber + " [GAME_ID]= " + game.gameID() + " [GAME_NAME]: " + game.gameName());
                System.out.println("  White Username: " + ((game.whiteUsername() == null) ? "[Available]" : game.whiteUsername()));
                System.out.println("  Black Username: " + ((game.blackUsername() == null) ? "[Available]" : game.blackUsername()) + "\n");
                gameNumber++;
            }
        }
    }

    public void joinGame() {
        System.out.println("\nPlease enter the number of the game you would like to join: ");
        String gameIDStr = scan.nextLine();
        System.out.println("Please enter which color you would like to play as (WHITE or BLACK)");
        String playerColor = scan.nextLine();
        try {
            int gameNum = Integer.parseInt(gameIDStr);
            if (!(playerColor.equalsIgnoreCase("WHITE") || playerColor.equalsIgnoreCase("BLACK")))
                invalidInput();
            else {
                JoinGameRes result = serverFacade.joinGame(gameNum, playerColor, authToken);
                if (result.getMessage() == null) {
                    // Set up game variables
                    this.gameID = gameNum;
                    this.teamColor = playerColor;
                    if (tryConnectToGame()) {
                        // The inGame() method will be called after receiving LOAD_GAME
                    }
                } else {
                    System.out.println(result.getMessage());
                }
            }
        } catch (NumberFormatException ex) {
            invalidInput();
        }
    }

    private void observeGame() {
        System.out.println("\nPlease enter the [GAME_ID] of the game you would like to observe: ");
        String gameIDStr = scan.nextLine();
        try {
            int gameNum = Integer.parseInt(gameIDStr);
            JoinGameRes result = serverFacade.joinGame(gameNum, "SPECTATOR", authToken);
            if (result.getMessage() == null) {
                this.gameID = gameNum;
                this.teamColor = "SPECTATOR";
                if (tryConnectToGame()) {

                }
            } else {
                System.out.println(result.getMessage());
            }
        } catch (NumberFormatException ex) {
            invalidInput();
        }
    }

    static void invalidInput() {
        System.out.println("\nInvalid input detected. Please follow these guidelines:");
        System.out.println("  - To select an option, input the number or the exact word of the option.");
        System.out.println("    Example: To access the help screen, enter '1' or 'help'.");
        System.out.println("  - To join or observe a game, input the game number only.");
        System.out.println("    Example: To join the game with ID '1', enter '1' without any spaces or additional characters.");
    }

    // Methods from Game class below

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
            gameLoadedLatch = new CountDownLatch(1);
            send(json);
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    switch (serverMessage.getServerMessageType()) {
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
                            if (teamColor.equalsIgnoreCase("SPECTATOR"))
                                listObserveOptions();
                            else
                                listGameOptions();
                            gameLoadedLatch.countDown();
                        }
                    }
                }
            });
            gameLoadedLatch.await();
            inGame(teamColor.equalsIgnoreCase("SPECTATOR"));
        } catch (Exception ex) {
            System.out.println("Error connecting to game: " + ex.getMessage());
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
//                listObserveOptions();
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
        System.out.println(EscapeSequences.SET_TEXT_COLOR_GREEN + "Please input the position of the piece you would like to see the valid moves of:" + EscapeSequences.RESET_TEXT_COLOR);
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
        } catch (Exception ex) {
            System.out.println("Invalid move");
            return;
        }

        startCol = charToInt(startPos.charAt(0));
        endCol = charToInt(endPos.charAt(0));

        if (startCol == -1 || endCol == -1) {
            System.out.println("Invalid move");
            return;
        }

        ChessPiece.PieceType pawnPromotion = null;
        if ((gameData.game().getBoard().getPiece(new ChessPosition(startRow, startCol)) != null)
                && (gameData.game().getBoard().getPiece(new ChessPosition(startRow, startCol)).getPieceType() == ChessPiece.PieceType.PAWN)
                && (endRow == 8)) {
            System.out.println("Please input pawn promotion (Queen, Rook, Bishop, or Knight)");
            String promotionInput = scan.nextLine();
            switch (promotionInput.toUpperCase()) {
                case "QUEEN":
                    pawnPromotion = ChessPiece.PieceType.QUEEN;
                    break;
                case "ROOK":
                    pawnPromotion = ChessPiece.PieceType.ROOK;
                    break;
                case "BISHOP":
                    pawnPromotion = ChessPiece.PieceType.BISHOP;
                    break;
                case "KNIGHT":
                    pawnPromotion = ChessPiece.PieceType.KNIGHT;
                    break;
                default:
                    System.out.println("Invalid promotion choice");
                    return;
            }
        }

        try {
            ChessMove move = new ChessMove(new ChessPosition(startRow, startCol), new ChessPosition(endRow, endCol), pawnPromotion);
            String json = new Gson().toJson(new MakeMove(authToken, gameID, move));
            send(json);
        } catch (Exception ex) {
            System.out.println("Make Move Message Sending Error");
        }
    }
}