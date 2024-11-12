package ui;
import chess.*;

public class BoardUI {
    ChessPiece[][] board;
    private static final String[] LETTERS = {" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};



    public BoardUI(ChessPiece[][] board){
        this.board = board;
    }



    private void printLetters(boolean isWhiteSide) {
        setBorderSquare();
        printEmptySpace();

        if (isWhiteSide) {
            for (String letter : LETTERS) {
                System.out.print(letter);
            }
        } else {
            for (int i = LETTERS.length - 1; i >= 0; i--) {
                System.out.print(LETTERS[i]);
            }
        }

        printEmptySpace();
    }



    private void printEmptySpace() {
        System.out.print("   ");
    }

    private void setBorderSquare() {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK + EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
    }
}
