import chess.*;
import clienttoserver.ServerFacade;
import model.GameData;
import result.*;
import ui.GameBoardUI;
import java.util.Scanner;

public class Main {

    static ServerFacade serverFacade;
    static String authToken;

    public static void main(String[] args) {

        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        //int port =
        serverFacade = new ServerFacade(8080);

        Scanner scan = new Scanner(System.in);
        String input = "start";
        boolean loggedIn = false;

        while(!(input.equalsIgnoreCase("quit") || input.equals("2"))) {
            System.out.println("\n-------------------");
            if(input.equals("start")) {
                chessClientStartUp();
            } else if(input.equals("help") || input.equals("1")) {
                helpExplainOptions(loggedIn);
            } else if(!loggedIn) {
                if(input.equalsIgnoreCase("register") || input.equals("3")) {
                    loggedIn = registerNewUser();
                } else if(input.equalsIgnoreCase("login") || input.equals("4")) {
                    loggedIn = loginUser();
                } else {
                    invalidInput();
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
                    observeGame();
                } else {
                    invalidInput();
                }
            }
            listOptions(loggedIn);
            System.out.println("\nPlease input your selection: ");
            input = scan.nextLine();
        }
    }


    static void chessClientStartUp() {
        System.out.println("~ Whats up! Welcome to Curt's Chess Game Server :)");
        System.out.println("Please enter the number or name of the option you would like to choose.");
        System.out.println("If you have any questions, please enter '1' or type 'Help' for assistance and explanations.");
    }

    static void listOptions(boolean loggedIn) {
        System.out.println("\n 1. Help");
        System.out.println(" 2. Quit");
        if(!loggedIn) {
            System.out.println(" 3. Register");
            System.out.println(" 4. Login");
        } else {
            System.out.println(" 3. Logout");
            System.out.println(" 4. Create Game");
            System.out.println(" 5. List Games");
            System.out.println(" 6. Play Game/Join Game");
            System.out.println(" 7. Observe Game");
        }
    }

    static void helpExplainOptions(boolean loggedIn) {
        System.out.println("\nEntering 1 or help will bring up this explanation again.");
        System.out.println("Entering 2 or quit will close down the program");
        if(!loggedIn) {
            System.out.println("To create a new user, simply enter ‘3’ or type ‘register’. You will then be prompted to provide a username, password, and email address.");
            System.out.println("Entering ‘4’ or ‘login’ will allow you to log in as an existing user. Simply input your username and password when prompted.");
        } else {
            System.out.println("To logout, enter '3' or 'logout'. No further input is needed.");
            System.out.println("To create a new game, enter '4' or 'create game'. You will need to input a game name.");
            System.out.println("To list all created games, enter '5' or 'list games'.");
            System.out.println("To join a game to play, enter '6' or 'play game'. You will need to input the game ID and your team color.");
            System.out.println("To observe a game, enter '7' or 'observe game'. You will need to input the game ID you want to watch.");
        }
    }

    static boolean registerNewUser() {
        Scanner scan = new Scanner(System.in);
        System.out.println("\nPlease enter your username: ");
        String username = scan.nextLine();
        System.out.println("Please enter your password: ");
        String password = scan.nextLine();
        System.out.println("Please enter your email: ");
        String email = scan.nextLine();
        RegisterResult result = serverFacade.registerUser(username, password, email);

        if(result.getAuthToken() == null) {
            System.out.println(result.getMessage());
            return false;
        } else {
            System.out.println("Successfully registered.");
            authToken = result.getAuthToken();
            return true;
        }
    }

//    static boolean loginUser() {
//        Scanner scan = new Scanner(System.in);
//        System.out.println("\nPlease enter your username: ");
//        String username = scan.nextLine();
//        System.out.println("Please enter your password: ");
//        String password = scan.nextLine();
//        LoginResult result = serverFacade.loginUser(username, password);
//
//        if(result.getAuthToken() == null) {
//            System.out.println(result.getMessage());
//            return false;
//        } else {
//            System.out.println("Successfully logged in.");
//            authToken = result.getAuthToken();
//            return true;
//        }
//    }

    static boolean logoutUser() {
        LogoutResult result = serverFacade.logoutUser(authToken);
        if(result.getMessage() == null) {
            System.out.println("Successfully logged out.");
            authToken = null;
            return false;
        } else {
            System.out.println(result.getMessage());
            return true;
        }
    }

    static void createGame() {
        Scanner scan = new Scanner(System.in);
        System.out.println("\nPlease enter the name of the game you wish to create: ");
        String gameName = scan.nextLine();
        CreateGameResult result = serverFacade.createGame(gameName, authToken);
        if(result.getGameID() == null) {
            System.out.println(result.getMessage());
        } else {
            System.out.println("Successfully created " + gameName + " on Game ID " + result.getGameID());
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
                JoinGameResult result = serverFacade.joinGame(gameNum, playerColor, authToken);
                if(result.getMessage() == null) {
                    System.out.println("Successfully joined game.");
                    printBoards();
                    inGame();
                } else {
                    System.out.println(result.getMessage());
                }
            }
        } catch(NumberFormatException ex) {
            invalidInput();
        }
    }

    static void listGames() {
        ListGamesResult result = serverFacade.listGames(authToken);
        if(result.getGames() == null) {
            System.out.println(result.getMessage());
        } else {
            System.out.println("List of games: ");
            for(GameData game : result.getGames()) {
                System.out.println("Game ID: " + game.gameID() + ", Game Name: " + game.gameName());
                System.out.println("  White Username: " + ((game.whiteUsername() == null) ? "<is Available>" : game.whiteUsername()));
                System.out.println("  Black Username: " + ((game.blackUsername() == null) ? "<is Available>" : game.blackUsername()) + "\n");
            }
        }
    }

    static void observeGame() {
        Scanner scan = new Scanner(System.in);
        System.out.println("\nPlease enter the gameID of the game you would like to observe: ");
        String gameID = scan.nextLine();
        try {
            int gameNum = Integer.parseInt(gameID);
            JoinGameResult result = serverFacade.joinGame(gameNum, "SPECTATOR", authToken);
            if(result.getMessage() == null) {
                System.out.println("Successfully joined game.");
                printBoards();
                inGame();
            } else {
                System.out.println(result.getMessage());
            }
        } catch(NumberFormatException ex) {
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





    static void printBoards() {
        ChessPiece[][] newBoard = new ChessGame().getBoard().getBoard();
        GameBoardUI gameBoard = new GameBoardUI(newBoard);
        System.out.println("White board: ");
        gameBoard.printWhiteSideBoard();
        System.out.println("\nBlack board: ");
        gameBoard.printBlackSideBoard();
    }

    static void inGame() {
        Scanner scan = new Scanner(System.in);
        String input = "start";
        while(!input.equalsIgnoreCase("quit")) {
            System.out.println("When you are ready to leave the game, please enter 'quit'");
            input = scan.nextLine();
        }
    }
}