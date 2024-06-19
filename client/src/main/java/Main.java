import chess.*;
import clienttoserver.ServerFacade;
import model.GameData;
import result.*;
import ui.GamePlayUI;

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

        while(!(input.equalsIgnoreCase("quit") || (input.equals("2") && !loggedIn))) {
            System.out.println("\n-----------------------");
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
                if(input.equalsIgnoreCase("logout") || input.equals("2")) {
                    loggedIn = logoutUser();
                } else if(input.equalsIgnoreCase("create game") || input.equals("3") || input.equalsIgnoreCase("create")) {
                    createGame();
                } else if(input.equalsIgnoreCase("list games") || input.equals("4") || input.equalsIgnoreCase("list")) {
                    listGames();
                } else if(input.equalsIgnoreCase("play game") || input.equals("5") || input.equalsIgnoreCase("play")) {
                    joinGame();
                } else if(input.equalsIgnoreCase("observe game") || input.equals("6") || input.equalsIgnoreCase("observe")) {
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
        System.out.println("~ Hi! Welcome to Curt's Chess Server! ~");
        System.out.println("Please select an option by typing the corresponding number or command:");
        System.out.println("For help at any time, type 'Help' or '1'.");
    }

    static void listOptions(boolean loggedIn) {
        System.out.println("\n 1. Help");
        if(!loggedIn) {
            System.out.println(" 2. Quit");
            System.out.println(" 3. Register");
            System.out.println(" 4. Login");
        } else {
            System.out.println(" 2. Logout");
            System.out.println(" 3. Create Game");
            System.out.println(" 4. List Games");
            System.out.println(" 5. Play Game");
            System.out.println(" 6. Observe Game");
        }
    }

    static void helpExplainOptions(boolean loggedIn) {
        System.out.println("\nTo view these instructions again, enter '1' or 'Help'.");

        if (!loggedIn) {
            System.out.println("2. Quit - Close the program.");
            System.out.println("3. Register - Create a new user account by providing a username, password, and email.");
            System.out.println("4. Login - Access your existing account by entering your username and password.");
        } else {
            System.out.println("2. Logout - Log out of your current session.");
            System.out.println("3. Create Game - Start a new game by entering a game name.");
            System.out.println("4. List Games - View a list of all available games.");
            System.out.println("5. Play Game - Join a game to play by providing the game ID and your team color.");
            System.out.println("6. Observe Game - Watch an ongoing game by entering the game ID.");
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

    static void invalidInput() {
        System.out.println("\nInvalid input detected.");
        System.out.println("To select an option, please enter the corresponding number or the exact text of the option.");
        System.out.println("  For example, to access the help screen, type '1' or 'Help'.");
        System.out.println("To join or observe a game, please enter only the game ID number.");
        System.out.println("  For example, to join the game with ID '1', simply type '1' without any additional characters or spaces.");
    }

    static boolean loginUser() {
        Scanner scan = new Scanner(System.in);
        System.out.println("\nPlease enter your username: ");
        String username = scan.nextLine();
        System.out.println("Please enter your password: ");
        String password = scan.nextLine();
        LoginResult result = serverFacade.loginUser(username, password);

        if(result.getAuthToken() == null) {
            System.out.println(result.getMessage());
            return false;
        } else {
            System.out.println("Successfully logged in.");
            authToken = result.getAuthToken();
            return true;
        }
    }

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
        System.out.println("\nPlease enter the game name you would like to create: ");
        String gameName = scan.nextLine();
        CreateGameResult result = serverFacade.createGame(gameName, authToken);
        if(result.getGameID() == null) {
            System.out.println(result.getMessage());
        } else {
            System.out.println("Successfully created \"" + gameName + "\", can be found with game number " + result.getGameID());
        }
    }

    static void listGames() {
        ListGamesResult result = serverFacade.listGames(authToken);
        if(result.getGames() == null) {
            System.out.println(result.getMessage());
        } else {
            System.out.println("List of games: ");
            for(GameData game : result.getGames()) {
                System.out.println(game.gameID() + ". Game Name: " + game.gameName());
                System.out.println("  White Username: " + ((game.whiteUsername() == null) ? "<Available>" : game.whiteUsername()));
                System.out.println("  Black Username: " + ((game.blackUsername() == null) ? "<Available>" : game.blackUsername()) + "\n");
            }
        }
    }

    static void joinGame() {
        Scanner scan = new Scanner(System.in);
        System.out.println("\nPlease enter the number of the game you would like to join: ");
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
                    GamePlayUI gameUI = new GamePlayUI(gameNum, playerColor, authToken);
                    if(gameUI.tryConnectToGame())
                        gameUI.inGame(false);
                } else {
                    System.out.println(result.getMessage());
                }
            }
        } catch(NumberFormatException ex) {
            invalidInput();
        }
    }

    static void observeGame() {
        Scanner scan = new Scanner(System.in);
        System.out.println("\nPlease enter the number of the game you would like to observe: ");
        String gameID = scan.nextLine();
        try {
            int gameNum = Integer.parseInt(gameID);

            //I might need to get rid of this joinGame command
            JoinGameResult result = serverFacade.joinGame(gameNum, "SPECTATOR", authToken);
            if(result.getMessage() == null) {
                GamePlayUI gameUI = new GamePlayUI(gameNum, "SPECTATOR", authToken);
                if(gameUI.tryConnectToGame())
                    gameUI.inGame(true);
            } else {
                System.out.println(result.getMessage());
            }
        } catch(NumberFormatException ex) {
            invalidInput();
        }

    }
}