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
            System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
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
        System.out.println("~ Hi! Welcome to the Chess Server! ~");
        System.out.println("Please input the number or name of what you would like to do.");
        System.out.println("If you have any questions, please enter 1 or Help for assistance and explanations.");
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
        System.out.println("\nEntering 1 or help will bring up this explanation again.");
        if(!loggedIn) {
            System.out.println("Entering 2 or quit will close down the program");
            System.out.println("Entering 3 or register will allow you to create a new user, you just need to input a username, password, and email.");
            System.out.println("Entering 4 or login will allow you to login an existing user, you just need to input your username and password.");
        } else {
            System.out.println("Entering 2 or logout will allow you to logout, no need to input anything.");
            System.out.println("Entering 3 or create game will allow you to create a new game, you just need to input a game name.");
            System.out.println("Entering 4 or list games will allow you to see all the created games.");
            System.out.println("Entering 5 or play game will allow you to join a game to play, you just need to input the gameID and team color you want to play.");
            System.out.println("Entering 6 or observe game will allow you to join a game to observe, you just need to input the gameID you want to watch.");
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
        System.out.println("\nInvalid input. If you are trying to select an option, Please input the number or the exact words of the option.");
        System.out.println("  For example: to access the help screen, please input '1' or 'help'.");
        System.out.println("If you are trying to join or observe a game, please make sure you only input the number of that game.");
        System.out.println("  For example: to join the game with ID of '1', please only enter '1', no spaces or anything but the number");
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