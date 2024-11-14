package ui;
import chess.ChessGame;
import chess.ChessPiece;
import facade.ServerFacade;
import models.GameData;
import result.*;

import java.util.Scanner;

public class PregameUI {

    static ServerFacade serverFacade;
    static String authToken;
    String input = "start";
    boolean loggedIn = false;

    public PregameUI(){
    }


    public void playChess() {
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.PAWN);
//        System.out.println("240 Chess Client: " + piece);
        serverFacade = new ServerFacade(8080);
        Scanner scan = new Scanner(System.in);

        boolean loggedIn = false;


        while(!(input.equalsIgnoreCase("quit") || input.equals("2"))) {
//
            if(input.equals("start")) {
                chessStart();
            } else if(input.equals("help") || input.equals("1")) {
                helpExplainOptions(loggedIn);
            } else if(!loggedIn) {
                if(input.equalsIgnoreCase("register") || input.equals("3")) {
                    loggedIn = registerUser();
                } else if(input.equalsIgnoreCase("login") || input.equals("4")) {
                    loggedIn = loginUser();
                } else {
//                    invalidInput();
                }
            } else { //loggedIn = true
                if(input.equalsIgnoreCase("logout") || input.equals("3")) {
                    loggedIn = logoutUser();
                } else if(input.equalsIgnoreCase("create game") || input.equals("4")) {
                    createGame();
                } else if(input.equalsIgnoreCase("list games") || input.equals("5")) {
                    listGames();
                } else if(input.equalsIgnoreCase("play game") || input.equals("6")) {
                    joinGame();
                } else if(input.equalsIgnoreCase("observe game") || input.equals("7")) {
//                    observeGame();
                } else {
                    invalidInput();
                }
            }
            System.out.println("\nPlease input your selection: ");
            input = scan.nextLine();
        }
    }



    static void chessStart() {
        System.out.println("\n** [WELCOME!] to Curt's Chess Game Server **");
        System.out.println(
                        EscapeSequences.SET_TEXT_COLOR_GREEN +
                        "\nfor [GAME_OPTIONS] press [1] " +
                        EscapeSequences.RESET_TEXT_COLOR
        );
    }

    static void helpExplainOptions(boolean loggedIn) {

        if(!loggedIn) {
            System.out.println(EscapeSequences.SET_TEXT_BOLD +
                    EscapeSequences.SET_TEXT_COLOR_BLUE + "\nto [REGISTER_USER] press [3] or enter 'register'. " +
                    "You will then be prompted to provide a username, password, and email address."
                    + EscapeSequences.RESET_TEXT_BOLD_FAINT +
                    EscapeSequences.RESET_TEXT_COLOR);
            System.out.println(EscapeSequences.SET_TEXT_BOLD +
                    EscapeSequences.SET_TEXT_COLOR_YELLOW +
                    "to [LOGIN] press [4] or enter 'login'. Simply input your username and password when prompted."
                    + EscapeSequences.RESET_TEXT_BOLD_FAINT +
                    EscapeSequences.RESET_TEXT_COLOR
            );
        } else {
            System.out.println("[LOGGED_IN >>>]");
            System.out.println("To [LOGOUT], press '3' or enter 'logout'.");
            System.out.println("To [CREATE_GAME] press '4' or enter 'create game'. You will need to input a game name.");
            System.out.println("To [LIST_GAME], press '5' or enter 'list games'.");
            System.out.println("To [PLAY_GAME], press '6' or enter 'play game'. You will need to input the game ID and your team color.");
            System.out.println("To [OBSERVER_GAME], press '7' or enter 'observe game'. You will need to input the game ID you want to watch.");
        }
        System.out.println("\npress [1] to see options again");
        System.out.println("press [2] to quit the game");
    }

    static boolean loginUser() {
        Scanner scan = new Scanner(System.in);
        System.out.println("\nPlease enter your [USERNAME]: ");
        String username = scan.nextLine();
        System.out.println("Please enter your [PASSWORD]: ");
        String password = scan.nextLine();
        LoginRes result = serverFacade.loginUser(username, password);

        if(result.getAuthToken() == null) {
            System.out.println(result.getMessage());
            return false;
        } else {
            System.out.println("Successfully logged in.");
            System.out.println("\n[LOGGED_IN >>> ]");
            authToken = result.getAuthToken();
            return true;
        }
    }

    static boolean logoutUser() {
        LogoutRes result = serverFacade.logoutUser(authToken);
        if(result.getMessage() == null) {
            System.out.println("Successfully logged out.");
            System.out.println("\n[LOGGED_OUT >>> ]");
            authToken = null;
            return false;
        } else {
            System.out.println(result.getMessage());
            return true;
        }
    }

    static boolean registerUser(){
        Scanner scan = new Scanner(System.in);
        System.out.println("\nPlease enter your [USERNAME]: ");
        String username = scan.nextLine();
        System.out.println("Please enter your [PASSWORD]: ");
        String password = scan.nextLine();
        System.out.println("Please enter your [EMAIL]: ");
        String email = scan.nextLine();
        RegisterRes result = serverFacade.registerUser(username, password, email);

        if(result.getAuthToken() == null) {
            System.out.println(result.getMessage());
            return false;
        } else {
            System.out.println("[REGISTERED_SUCCESS]" + username);
            authToken = result.getAuthToken();
            return true;
        }
    }

    static void createGame() {
        Scanner scan = new Scanner(System.in);
        System.out.println("\nPlease enter the [game name] you would like to create: ");
        String gameName = scan.nextLine();
        CreateGameRes result = serverFacade.createGame(gameName, authToken);
        if(result.getGameId() == null) {
            System.out.println(result.getMessage());
        } else {
            System.out.println("Successfully created \"" + gameName + "\", can be found with [gameID] " + result.getGameId());
        }
    }

    static void listGames() {
        ListGamesRes result = serverFacade.listGames(authToken);
        if(result.getGames() == null) {
            System.out.println(result.getMessage());
        } else {
            System.out.println("List of games: ");
            for(GameData game : result.getGames()) {
                System.out.println("[GAME_ID]= "+ game.gameID() + " [GAME_NAME]: " + game.gameName());
                System.out.println("  White Username: " + ((game.whiteUsername() == null) ? "<Available>" : game.whiteUsername()));
                System.out.println("  Black Username: " + ((game.blackUsername() == null) ? "<Available>" : game.blackUsername()) + "\n");
            }
        }
    }

    static void joinGame() {
        Scanner scan = new Scanner(System.in);
        System.out.println("\nPlease enter the gameID of the game you would like to join: ");
        String gameID = scan.nextLine();
        System.out.println("Please enter which color you would like to play as (WHITE or BLACK)");
        String playerColor = scan.nextLine();
        try {
            int gameNum = Integer.parseInt(gameID);
            if(!(playerColor.equalsIgnoreCase("WHITE") || playerColor.equalsIgnoreCase("BLACK")))
                invalidInput();
            else {
                JoinGameRes result = serverFacade.joinGame(gameNum, playerColor, authToken);
                if(result.getMessage() == null) {
                    System.out.println("Successfully joined game.");
                    printBoards();
                    playerInGame();
                } else {
                    System.out.println(result.getMessage());
                }
            }
        } catch(NumberFormatException ex) {
            invalidInput();
        }
    }

    static void playerInGame() {
        Scanner scan = new Scanner(System.in);
        String input = "start";
        while(!input.equalsIgnoreCase("quit")) {
            System.out.println("When you are ready to leave the game, please enter ['quit']");
            input = scan.nextLine();
        }
    }

    static void printBoards(){
        ChessPiece[][] newBoard = new ChessGame().getBoard().getBoard();
        BoardUI gameBoard = new BoardUI(newBoard);
        System.out.println("[WHITE_BOARD...] ");
        gameBoard.printBoard(true);
        System.out.println("\n[BLACK_BOARD...]");
        gameBoard.printBoard(false);
    }

    static void invalidInput() {
        System.out.println("\nInvalid input detected. Please follow these guidelines:");
        System.out.println("  - To select an option, input the number or the exact word of the option.");
        System.out.println("    Example: To access the help screen, enter '1' or 'help'.");
        System.out.println("  - To join or observe a game, input the game number only.");
        System.out.println("    Example: To join the game with ID '1', enter '1' without any spaces or additional characters.");
    }











}
