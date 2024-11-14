package ui;
import chess.ChessGame;
import chess.ChessPiece;
import facade.ServerFacade;
import models.GameData;
import result.*;

import java.util.Scanner;

public class PregameUI {

    private ServerFacade serverFacade;
    private String authToken;
    private Scanner scan;

    public PregameUI() {
        serverFacade = new ServerFacade(8080);
        scan = new Scanner(System.in);
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
//                helpExplainOptions(loggedIn);
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
            System.out.println("\nTo " + EscapeSequences.SET_TEXT_BOLD + EscapeSequences.SET_TEXT_COLOR_BLUE + "[REGISTER_USER] press [3]" + EscapeSequences.RESET_TEXT_BOLD_FAINT + EscapeSequences.RESET_TEXT_COLOR + " or enter 'register'.");
            System.out.println("You will then be prompted to provide a username, password, and email address.");
            System.out.println("To " + EscapeSequences.SET_TEXT_BOLD + EscapeSequences.SET_TEXT_COLOR_YELLOW + "[LOGIN] press [4]" + EscapeSequences.RESET_TEXT_BOLD_FAINT + EscapeSequences.RESET_TEXT_COLOR + " or enter 'login'.");
            System.out.println("Simply input your username and password when prompted.");
        } else {
            System.out.println(EscapeSequences.SET_TEXT_COLOR_GREEN + "[LOGGED_IN >>>]" + EscapeSequences.RESET_TEXT_COLOR);
            System.out.println("To " + EscapeSequences.SET_TEXT_COLOR_RED + "[LOGOUT]" + EscapeSequences.RESET_TEXT_COLOR + ", press '3' or enter 'logout'.");
            System.out.println("To " + EscapeSequences.SET_TEXT_COLOR_YELLOW + "[CREATE_GAME]" + EscapeSequences.RESET_TEXT_COLOR + ", press '4' or enter 'create game'. You will need to input a game name.");
            System.out.println("To " + EscapeSequences.SET_TEXT_COLOR_BLUE + "[LIST_GAME]" + EscapeSequences.RESET_TEXT_COLOR + ", press '5' or enter 'list games'.");
            System.out.println("To " + EscapeSequences.SET_TEXT_COLOR_ORANGE + "[JOIN_GAME]" + EscapeSequences.RESET_TEXT_COLOR + ", press '6' or enter 'join game'. You will need to input the game ID and your team color.");
            System.out.println("To " + EscapeSequences.SET_TEXT_COLOR_WHITE + "[OBSERVER_GAME]" + EscapeSequences.RESET_TEXT_COLOR + ", press '7' or enter 'observe game'. You will need to input the game ID you want to watch.");
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
            System.out.println("[LOGGED_IN]" + " " +  "as" + " " + username);
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
            for (GameData game : result.getGames()) {
                System.out.println("[GAME_ID]= " + game.gameID() + " [GAME_NAME]: " + game.gameName());
                System.out.println("  White Username: " + ((game.whiteUsername() == null) ? "[Available]" : game.whiteUsername()));
                System.out.println("  Black Username: " + ((game.blackUsername() == null) ? "[Available]" : game.blackUsername()) + "\n");
            }
        }
    }

    private void joinGame() {
        System.out.println("\nPlease enter the [GAME_ID] of the game you would like to join: ");
        String gameID = scan.nextLine();
        System.out.println("Please enter which color you would like to play as (WHITE or BLACK): ");
        String playerColor = scan.nextLine();
        try {
            int gameNum = Integer.parseInt(gameID);
            if (!playerColor.equalsIgnoreCase("WHITE") && !playerColor.equalsIgnoreCase("BLACK")) {
                invalidInput();
            } else {
                JoinGameRes result = serverFacade.joinGame(gameNum, playerColor.toUpperCase(), authToken);
                if (result.getMessage() == null) {
                    System.out.println("Successfully joined game.");
                    printBoards();
                    playerInGame();
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
        String gameID = scan.nextLine();
        try {
            int gameNum = Integer.parseInt(gameID);
            JoinGameRes result = serverFacade.joinGame(gameNum, "SPECTATOR", authToken);
            if(result.getMessage() == null) {
                System.out.println("Successfully joined game.");
                System.out.println("Observing game ID: " + gameNum);
                printBoards();
                playerInGame();
            } else {
                System.out.println(result.getMessage());
            }
        } catch (NumberFormatException ex) {
            invalidInput();
        }
    }

    private void playerInGame() {
        String input = "";
        while (!input.equalsIgnoreCase("quit")) {
            System.out.println("When you are ready to leave the game, please enter 'quit':");
            input = scan.nextLine();
        }
    }

    private void printBoards() {
        ChessPiece[][] newBoard = new ChessGame().getBoard().getBoard();
        BoardUI gameBoard = new BoardUI(newBoard);
        System.out.println("[WHITE_BOARD...] ");
        gameBoard.printBoard(true);
        System.out.println("\n[BLACK_BOARD...]");
        gameBoard.printBoard(false);
    }

    private void invalidInput() {
        System.out.println("\nInvalid input detected. Please follow these guidelines:");
        System.out.println("  - To select an option, input the number or the exact word of the option.");
        System.out.println("    Example: To access the help screen, enter '1' or 'help'.");
        System.out.println("  - To join or observe a game, input the game number only.");
        System.out.println("    Example: To join the game with ID '1', enter '1' without any spaces or additional characters.");
    }
}