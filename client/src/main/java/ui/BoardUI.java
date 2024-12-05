package ui;
import chess.*;

import java.util.Collection;

import static ui.EscapeSequences.SET_TEXT_COLOR_YELLOW;


public class BoardUI {

    private ChessPiece[][] board;
    private static final String[] LETTERS = {" A ", " B ", " C ", " D ", " E ", " F ", " G ", " H "};
    private Collection<ChessMove> validMoves;
    private ChessPosition selectedPiece;

    public BoardUI(ChessPiece[][] board) {
        this.board = board;
    }

    public void printBoard(boolean isWhiteSide) {
        printLetters(isWhiteSide);
        System.out.println();
//        isWhiteSide = true = white
//        isWhiteSide = false = black
        if (isWhiteSide) {
            for (int row = 8; row >= 1; row--) {
                printRow(row, isWhiteSide);
                System.out.println();
            }
        } else {
            for (int row = 1; row <= 8; row++) {
                printRow(row, isWhiteSide);
                System.out.println();
            }
        }
        printLetters(isWhiteSide);
        System.out.println();
    }

    private void printLetters(boolean isWhiteSide) {
        setTextColor();
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
        resetTextColor();
    }

    private void printRow(int row, boolean isWhiteSide) {
        printEdge(row);
        if (isWhiteSide) {
            for (int col = 1; col <= 8; col++) {
                setBackgroundColor(row, col);
                setTextColor(row, col);
                printPieceType(row, col);
            }
        } else {
            for (int col = 8; col >= 1; col--) {
                setBackgroundColor(row, col);
                setTextColor(row, col);
                printPieceType(row, col);
            }
        }
        printEdge(row);
        resetTextColor();
    }

    private void printEdge(int row) {
        setTextColor();
        System.out.print(" " + row + " ");
    }

    private void printPieceType(int row, int col) {
        boolean isValidDestination = false;
        if (validMoves != null) {
            for (ChessMove move : validMoves) {
                ChessPosition endPos = move.getEndPosition();
                if (endPos.getRow() == row && endPos.getColumn() == col) {
                    isValidDestination = true;
                    break;
                }
            }
        }

        if (isValidDestination) {
            System.out.print(SET_TEXT_COLOR_YELLOW); // Set text color to yellow
        }

        ChessPiece piece = board[row - 1][col - 1];
        if (piece == null) {
            printEmptySpace();
        } else {
            switch (piece.getPieceType()) {
                case PAWN -> System.out.print(" P ");
                case KNIGHT -> System.out.print(" N ");
                case ROOK -> System.out.print(" R ");
                case BISHOP -> System.out.print(" B ");
                case QUEEN -> System.out.print(" Q ");
                case KING -> System.out.print(" K ");
                default -> printEmptySpace();
            }
        }

        if (isValidDestination) {
            resetTextColor();
        }
    }

    public void setValidMoves(Collection<ChessMove> validMoves) {
        this.validMoves = validMoves;
    }

    public void setSelectedPiece(ChessPosition startPos) {
        selectedPiece = startPos;
    }

    private void printEmptySpace() {
        System.out.print("   ");
    }

    private void setTextColor() {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK + EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
    }

    private void resetTextColor() {
        System.out.print(EscapeSequences.RESET_TEXT_COLOR + EscapeSequences.RESET_BG_COLOR);
    }

    private void setBackgroundColor(int row, int col) {
        boolean isValidDestination = false;
        if (validMoves != null) {
            // Check if any valid move ends at (row, col)
            for (ChessMove move : validMoves) {
                ChessPosition endPos = move.getEndPosition();
                if (endPos.getRow() == row && endPos.getColumn() == col) {
                    isValidDestination = true;
                    break;
                }
            }
        }

        if (isValidDestination) {
            // Set background to yellow if it's a valid move destination
            System.out.print("\u001B[43m"); // ANSI for yellow background
        } else {
            // Default black/white pattern
            if ((row + col) % 2 == 0) {
                System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
            } else {
                System.out.print(EscapeSequences.SET_BG_COLOR_WHITE);
            }
        }
    }

    private void setTextColor(int row, int col) {
        ChessPiece piece = board[row - 1][col - 1];
        if (piece != null) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
            } else {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
            }
        }
    }
}