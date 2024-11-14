package ui;
import chess.ChessGame;
import chess.ChessPiece;
import facade.ServerFacade;
import result.*;

import java.util.Scanner;

public class PregameUI {

    static ServerFacade serverFacade;
    static String authToken;
    String input = "start";

    public PregameUI() {
        serverFacade = new ServerFacade(8080);
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.PAWN);
        System.out.println("240 Chess Client: " + piece);
        serverFacade = new ServerFacade(8080);
        Scanner scan = new Scanner(System.in);

        boolean loggedIn = false;


        while(!(input.equalsIgnoreCase("quit") || input.equals("2"))) {
            System.out.println("[LOGGED_OUT >>>]");
            if(input.equals("start")) {
                chessStart();
            } else if(input.equals("help") || input.equals("1")) {
                helpExplainOptions(loggedIn);
            } else if(!loggedIn) {
                if(input.equalsIgnoreCase("register") || input.equals("3")) {
                    loggedIn = registerUser();
                } else if(input.equalsIgnoreCase("login") || input.equals("4")) {
//                    loggedIn = loginUser();
                } else {
//                    invalidInput();
                }
            } else { //loggedIn = true
                if(input.equalsIgnoreCase("logout") || input.equals("3")) {
//                    loggedIn = logoutUser();
                } else if(input.equalsIgnoreCase("create game") || input.equals("4")) {
//                    createGame();
                } else if(input.equalsIgnoreCase("list games") || input.equals("5")) {
//                    listGames();
                } else if(input.equalsIgnoreCase("play game") || input.equals("6")) {
//                    joinGame();
                } else if(input.equalsIgnoreCase("observe game") || input.equals("7")) {
//                    observeGame();
                } else {
//                    invalidInput();
                }
            }
//            listOptions(loggedIn);
            System.out.println("\nPlease input your selection: ");
            input = scan.nextLine();
        }
    }

    static void chessStart() {
        System.out.println("[LOGGED_OUT >>>]");
        System.out.println("~ Whats up! Welcome to Curt's Chess Game Server :)");
        System.out.println("Please enter the number or name of the option you would like to choose.");
        System.out.println("If you have any questions, please enter '1' or type 'Help' for assistance and explanations.");
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


    static boolean registerUser(){
        Scanner scan = new Scanner(System.in);
        System.out.println("\nPlease enter your username: ");
        String username = scan.nextLine();
        System.out.println("Please enter your password: ");
        String password = scan.nextLine();
        System.out.println("Please enter your email: ");
        String email = scan.nextLine();
        RegisterRes result = serverFacade.registerUser(username, password, email);

        if(result.getAuthToken() == null) {
            System.out.println(result.getMessage());
            return false;
        } else {
            System.out.println("Successfully registered.");
            authToken = result.getAuthToken();
            return true;
        }
    }








}
