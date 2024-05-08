package chess;

import java.util.Collection;
import java.util.ArrayList;

public class RookMovesCalculator {

    Collection<ChessMove> ValidMovesCalculator;

    public RookMovesCalculator(ChessBoard board, ChessPosition startPosition) {

        ValidMovesCalculator = new ArrayList<>();
        ValidMovesCalculator validMove = new ValidMovesCalculator();

        //Right
        boolean validPosition = true;
        int i = 1;
        while(validPosition) {
            ChessPosition endPosition = new ChessPosition( startPosition.getRow(), startPosition.getColumn() + i);
            if (endPosition.getColumn() > 8) {
                validPosition = false;
            } else {
                validPosition = validMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
            }
            i++;
        }

        //Left
        validPosition = true;
        i = 1;
        while(validPosition) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow(), startPosition.getColumn() - i);
            if(endPosition.getColumn() < 1) {
                validPosition = false;
            } else {
                validPosition = validMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
            }
            i++;
        }

        //Down
        validPosition = true;
        i = 1;
        while(validPosition) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() - i, startPosition.getColumn());
            if(endPosition.getRow() < 1) {
                validPosition = false;
            } else {
                validPosition = validMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
            }
            i++;
        }

        //Up
        validPosition = true;
        i = 1;
        while(validPosition) {
            ChessPosition endPosition = new ChessPosition( startPosition.getRow() + i, startPosition.getColumn());
            if (endPosition.getRow() > 8) {
                validPosition = false;
            } else {
                validPosition = validMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
            }
            i++;
        }
    }

    public Collection<ChessMove> getRookMoves() {
        return ValidMovesCalculator;
    }
}