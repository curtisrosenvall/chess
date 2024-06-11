import Clienter2Sever.ServerFacade;
import chess.*;
import result.RegisterResult;
import ui.GameBoardUI;

import java.util.Scanner;

public class Main {

    static ServerFacade serverFacade;
    static String authToken;


    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        serverFacade = new ServerFacade(8080);

        Scanner scanner = new Scanner(System.in);
        String input = "start";
        boolean loggedIn = false;

        while(!(input.equalsIgnoreCase("quit") || input.equals("2"))) {
            System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            if (input.equals("start")) {
                chessClientStartUp();
            } else if (input.equals("help") || input.equals("1")) {
                helpExplainOptions(loggedIn);
            } else if (!loggedIn) {
                if (input.equalsIgnoreCase("register") || input.equals("3")) {
                    loggedIn = registerNewUser();
                } else if (input.equalsIgnoreCase("login") || input.equals("4")) {
                    loggedIn = loginUser();
                } else {
                    invalidInput();
                }
            } else { //loggedIn = true
                if (input.equalsIgnoreCase("logout") || input.equals("3")) {
                    loggedIn = logoutUser();
                } else if (input.equalsIgnoreCase("create game") || input.equals("4")) {
                    createGame();
                } else if (input.equalsIgnoreCase("list games") || input.equals("5")) {
                    listGames();
                } else if (input.equalsIgnoreCase("play game") || input.equals("6")) {
                    joinGame();
                } else if (input.equalsIgnoreCase("observe game") || input.equals("7")) {
                    observeGame();
                } else {
                    invalidInput();
                }
            }
            listOptions(loggedIn);
            System.out.println("\nPlease input your selection: ");
            input = scanner.nextLine();
        }
    }

    static boolean registerNewUser() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter your username: ");
        String username = scan.nextLine();
        System.out.println("Please enter your password: ");
        String password = scan.nextLine();
        System.out.println("Please enter your email: ");
        String email = scan.nextLine();
        RegisterResult result = serverFacade.registerUser(username,password,email);

        if(result.getAuthToken() == null) {
            System.out.println(result.getMessage());
            return false;
        } else {
            System.out.println("Successfully registered.");
            authToken = result.getAuthToken();
            return true;
        }
    }



    static void helpExplainOptions(boolean loggedIn) {
        System.out.println("\nEntering 1 or help will bring up this explanation again.");
        System.out.println("Entering 2 or quit will close down the program");
        if(!loggedIn) {
            System.out.println("Entering 3 or register will allow you to create a new user, you just need to input a username, password, and email.");
            System.out.println("Entering 4 or login will allow you to login an existing user, you just need to input your username and password.");
        } else {
            System.out.println("Entering 3 or logout will allow you to logout, no need to input anything.");
            System.out.println("Entering 4 or create game will allow you to create a new game, you just need to input a game name.");
            System.out.println("Entering 5 or list games will allow you to see all the created games.");
            System.out.println("Entering 6 or play game will allow you to join a game to play, you just need to input the gameID and team color you want to play.");
            System.out.println("Entering 7 or observe game will allow you to join a game to observe, you just need to input the gameID you want to watch.");
        }
    }

    static void chessClientStartUp() {
        System.out.println("~ Hi! Welcome to the Chess Server! ~");
        System.out.println("Please input the number or name of what you would like to do.");
        System.out.println("If you have any questions, please enter 1 or Help for assistance and explanations.");
    }
//    static void printBoard(){
//        ChessPiece[][] newBoard = new ChessGame().getBoard().getBoard();
//        GameBoardUI gameBoard = new GameBoardUI(newBoard);
//        System.out.println("White Board:");
//        gameBoard.printWhiteSideBoard();
//        System.out.println("Black Board:");
//        gameBoard.printBlackSideBoard();
//    }
}