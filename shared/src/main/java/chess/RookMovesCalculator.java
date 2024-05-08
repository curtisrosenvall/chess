package chess;

import java.util.Collection;
import java.util.ArrayList;

public class RookMovesCalculator {

    Collection<ChessMove> ValidMovesCalculator;

    public RookMovesCalculator(ChessBoard board, ChessPosition startPosition) {

        ValidMovesCalculator = new ArrayList<>();
        ValidMovesCalculator testMove = new ValidMovesCalculator();

        //Right
        boolean valid = true;
        int i = 1;
        while(valid) {
            ChessPosition endPosition = new ChessPosition( startPosition.getRow(), startPosition.getColumn() + i);
            if (endPosition.getColumn() > 8) {
                valid = false;
            } else {
                valid = testMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
            }
            i++;
        }

        //Left
        valid = true;
        i = 1;
        while(valid) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow(), startPosition.getColumn() - i);
            if(endPosition.getColumn() < 1) {
                valid = false;
            } else {
                valid = testMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
            }
            i++;
        }

        //Down
        valid = true;
        i = 1;
        while(valid) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() - i, startPosition.getColumn());
            if(endPosition.getRow() < 1) {
                valid = false;
            } else {
                valid = testMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
            }
            i++;
        }

        //Up
        valid = true;
        i = 1;
        while(valid) {
            ChessPosition endPosition = new ChessPosition( startPosition.getRow() + i, startPosition.getColumn());
            if (endPosition.getRow() > 8) {
                valid = false;
            } else {
                valid = testMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
            }
            i++;
        }
    }

    public Collection<ChessMove> getRookMoves() {
        return ValidMovesCalculator;
    }
}