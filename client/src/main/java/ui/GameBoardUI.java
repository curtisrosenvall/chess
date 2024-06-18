package ui;

import chess.*;
import java.util.ArrayList;
import java.util.Collection;

public class GameBoardUI {

    private final ChessPiece[][] board;
    private final String[] letters;
    private Collection<ChessMove> validMoves;
    private ChessPosition selectedPiece;

    public GameBoardUI(ChessPiece[][] board) {
        this.board = board;
        letters = new String[] {"\u2003A ", "\u2003B ", "\u2003C ", "\u2003D ", "\u2003E ", "\u2003F ", "\u2003G ", "\u2003H "};
    }

    public void printWhiteSideBoard(boolean showValidMoves) {
        printLettersWhite();
        System.out.println();
        for(int i = 8; i >= 1; i--) {
            printRowWhite(i, showValidMoves);
            System.out.println();
        }
        printLettersWhite();
        System.out.println();
    }

    public void printBlackSideBoard(boolean showValidMoves) {
        printLettersBlack();
        System.out.println();
        for(int i = 1; i <= 8; i++) {
            printRowBlack(i, showValidMoves);
            System.out.println();
        }
        printLettersBlack();
        System.out.println();
    }

    public void setBorderSquare() {
        System.out.printf(EscapeSequences.SET_TEXT_COLOR_BLACK + EscapeSequences.SET_BG_COLOR_WHITE);
    }

    public void setSquareToNormal() {
        System.out.printf(EscapeSequences.RESET_TEXT_COLOR + EscapeSequences.RESET_BG_COLOR);
    }

    public void printWhitePieces() {
        System.out.printf(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }

    public void printBlackPieces() {
        System.out.printf(EscapeSequences.SET_TEXT_COLOR_BLACK);
    }

    public void setBackGroundDark() {
        System.out.printf(EscapeSequences.SET_BG_COLOR_DARK_GREY);
    }

    public void setBackGroundValidDark() {
        System.out.printf(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
    }

    public void setBackGroundLight() {
        System.out.printf(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
    }

    public void setBackGroundValidLight() {
        System.out.printf(EscapeSequences.SET_BG_COLOR_GREEN);
    }

    public void setSelectedPieceBackGround() {
        System.out.printf(EscapeSequences.SET_BG_COLOR_RED);
    }

    public void setBackGroundColor(int row, int col) {
        if(((row + col) % 2) == 0)
            setBackGroundDark();
        else
            setBackGroundLight();
    }

    public void setValidBackGroundColor(int row, int col) {
        ArrayList<ChessPosition> endPositions = new ArrayList<>();
        for(ChessMove move : validMoves) {
            endPositions.add(move.getEndPosition());
        }
        if(selectedPiece.equals(new ChessPosition(row, col)))
            setSelectedPieceBackGround();
        else if(endPositions.contains(new ChessPosition(row, col))) {
            if(((row + col) % 2) == 0)
                setBackGroundValidDark();
            else
                setBackGroundValidLight();
        } else {
            setBackGroundColor(row, col);
        }
    }

    public void printEmptySpace() {
        System.out.print(EscapeSequences.EMPTY);
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

    public void printRowWhite(int row, boolean showValidMoves) {
        printEdge(row);
        for(int i = 1; i <= 8; i++) {
            if(showValidMoves)
                setValidBackGroundColor(row, i);
            else
                setBackGroundColor(row, i);
            printPieceType(row, i);
        }
        printEdge(row);
    }

    public void printRowBlack(int row, boolean showValidMoves) {
        printEdge(row);
        for(int i = 8; i >= 1; i--) {
            if(showValidMoves)
                setValidBackGroundColor(row, i);
            else
                setBackGroundColor(row, i);
            printPieceType(row, i);
        }
        printEdge(row);
    }

    public void printEdge(int row) {
        setBorderSquare();
        System.out.print("\u2003" + row + " ");
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

    public void printPieceType(int row, int col) {
        ChessPiece piece = board[row-1][col-1];
        if(piece == null) {
            printEmptySpace();
        } else if(piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            printWhitePieces();
            switch(piece.getPieceType()) {
                case PAWN -> System.out.print(EscapeSequences.WHITE_PAWN);
                case QUEEN -> System.out.print(EscapeSequences.WHITE_QUEEN);
                case KNIGHT -> System.out.print(EscapeSequences.WHITE_KNIGHT);
                case ROOK -> System.out.print(EscapeSequences.WHITE_ROOK);
                case BISHOP -> System.out.print(EscapeSequences.WHITE_BISHOP);
                default -> System.out.print(EscapeSequences.WHITE_KING);
            }
            setSquareToNormal();
        } else if(piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            printBlackPieces();
            switch(piece.getPieceType()) {
                case PAWN -> System.out.print(EscapeSequences.BLACK_PAWN);
                case QUEEN -> System.out.print(EscapeSequences.BLACK_QUEEN);
                case KNIGHT -> System.out.print(EscapeSequences.BLACK_KNIGHT);
                case ROOK -> System.out.print(EscapeSequences.BLACK_ROOK);
                case BISHOP -> System.out.print(EscapeSequences.BLACK_BISHOP);
                default -> System.out.print(EscapeSequences.BLACK_KING);
            }
            setSquareToNormal();
        }
    }

    public void setValidMoves(Collection<ChessMove> validMoves) {
        this.validMoves = validMoves;
    }

    public void setSelectedPiece(ChessPosition startPos) {
        selectedPiece = startPos;
    }
}
