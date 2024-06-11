package ui;

import chess.ChessGame;
import chess.ChessPiece;

public class GameBoardUI {

    ChessPiece[][] board;
    String[] letters;

    public GameBoardUI(ChessPiece[][] board) {
        this.board = board;
        this.letters = new String[] {"A", "B", "C", "D", "E", "F", "G", "H"};
    }


    public void printEmptySpace() {
        System.out.print("   ");
    }

    public void printEdge(int row) {
        setBorderSquare();
        System.out.print(" " + row + " ");
    }

    public void printBlackSideBoard() {
        printLettersBlack();
        System.out.println();
        for(int i = 0; i <= 8; i++) {
            printRowBlack(i);
            System.out.println();
        }
    }

    public void printWhiteSideBoard() {
        printLettersWhite();
        System.out.println();
        for(int i = 8; i >= 1; i--) {
            printRowWhite(i);
            System.out.println();
        }
        printLettersWhite();
        System.out.println();
    }

    public void printLettersWhite() {
        setBorderSquare();
        printEmptySpace();
        for(int i = 0; i < 8; i++) {
            System.out.print(letters[i]);
        }
        printEmptySpace();
        setSquareToNormal();
    }

    public void printLettersBlack() {
        setBorderSquare();
        printEmptySpace();
        for(int i = 7; i >= 0; i--) {
            System.out.print(letters[i]);
        }
        printEmptySpace();
        setSquareToNormal();
    }

    public void printRowBlack(int row) {
        printEdge(row);
        for(int i = 8; i >= 1; i--) {
            setBackGroundColor(row, i);
            setTextColor(row, i);
            printPieceType(row, i);
        }
        printEdge(row);
        setSquareToNormal();
    }

    public void printRowWhite(int row) {
        printEdge(row);
        for(int i = 1; i <= 8; i++) {
            setBackGroundColor(row, i);
            setTextColor(row, i);
            printPieceType(row, i);
        }
        printEdge(row);
        setSquareToNormal();
    }

    public void printPieceType(int row, int col) {
        if(board[row-1][col-1] == null) {
            printEmptySpace();
        } else if(board[row-1][col-1].getPieceType() == ChessPiece.PieceType.PAWN) {
            System.out.print(" P ");
        } else if(board[row-1][col-1].getPieceType() == ChessPiece.PieceType.KNIGHT) {
            System.out.print(" N ");
        } else if(board[row-1][col-1].getPieceType() == ChessPiece.PieceType.ROOK) {
            System.out.print(" R ");
        } else if(board[row-1][col-1].getPieceType() == ChessPiece.PieceType.BISHOP) {
            System.out.print(" B ");
        } else if(board[row-1][col-1].getPieceType() == ChessPiece.PieceType.QUEEN) {
            System.out.print(" Q ");
        } else {
            System.out.print(" K ");
        }
    }



    public void setTextColor(int row, int col) {
        if(board[row-1][col-1] != null) {
            if(board[row-1][col-1].getTeamColor() == ChessGame.TeamColor.WHITE) {
                setTextWhiteTeam();
            } else {
                setTextBlackTeam();
            }
        }
    }

    public void setBorderSquare() {
        System.out.printf(EscapeSequences.SET_TEXT_COLOR_BLACK + EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
    }

    public void setSquareToNormal() {
        System.out.printf(EscapeSequences.RESET_TEXT_COLOR + EscapeSequences.RESET_BG_COLOR);
    }

    public void setBackGroundDark() {
        System.out.printf(EscapeSequences.SET_BG_COLOR_BLACK);
    }

    public void setBackGroundLight() {
        System.out.printf(EscapeSequences.SET_BG_COLOR_WHITE);
    }

    public void setBackGroundColor(int row, int col) {
        if(((row + col) % 2) == 0)
            setBackGroundDark();
        else
            setBackGroundLight();
    }

    public void setTextWhiteTeam() {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
    }

    public void setTextBlackTeam() {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
    }




}
