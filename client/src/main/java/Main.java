import Clienter2Sever.ServerFacade;
import chess.*;
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
    }

    static void printBoard(){
        ChessPiece[][] newBoard = new ChessGame().getBoard().getBoard();
        GameBoardUI gameBoard = new GameBoardUI(newBoard);
        System.out.println("White Board:");
        gameBoard.printWhiteSideBoard();
        System.out.println("Black Board:");
        gameBoard.printBlackSideBoard();
    }
}