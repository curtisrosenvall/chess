package chess;

import java.util.Collection;
import java.util.ArrayList;

public class QueenMovesCalculator {

    Collection<ChessMove> ValidMovesCalculator;

    public QueenMovesCalculator(ChessBoard board, ChessPosition startPosition) {

        ValidMovesCalculator = new ArrayList<>();
        ValidMovesCalculator testMove = new ValidMovesCalculator();

        //^
        boolean valid = true;
        int i = 1;
        while(valid) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() + i, startPosition.getColumn());
            if (endPosition.getRow() > 8) {
                valid = false;
            } else {
                valid = testMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
            }
            i++;
        }

        //->
        valid = true;
        i = 1;
        while(valid) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow(), startPosition.getColumn() + i);
            if (endPosition.getColumn() > 8) {
                valid = false;
            } else {
                valid = testMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
            }
            i++;
        }

        //^ ->
        valid = true;
        i = 1;
        while(valid) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() + i, startPosition.getColumn() + i);
            if ((endPosition.getRow() > 8) || (endPosition.getColumn() > 8)) {
                valid = false;
            } else {
                valid = testMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
            }
            i++;
        }



        //D ->
        valid = true;
        i = 1;
        while(valid) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() - i, startPosition.getColumn() + i);
            if ((endPosition.getRow() < 1) || (endPosition.getColumn() > 8)) {
                valid = false;
            } else {
                valid = testMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
            }
            i++;
        }

        //^ ->
        valid = true;
        i = 1;
        while(valid) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() + i, startPosition.getColumn() - i);
            if((endPosition.getRow() > 8) || (endPosition.getColumn() < 1)) {
                valid = false;
            } else {
                valid = testMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
            }
            i++;
        }

        //D
        valid = true;
        i = 1;
        while(valid) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() - i, startPosition.getColumn());
            if (endPosition.getRow() < 1) {
                valid = false;
            } else {
                valid = testMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
            }
            i++;
        }

        //D <-
        valid = true;
        i = 1;
        while(valid) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() - i, startPosition.getColumn() - i);
            if ((endPosition.getRow() < 1) || (endPosition.getColumn() < 1)) {
                valid = false;
            } else {
                valid = testMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
            }
            i++;
        }

        //<-
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


    }

    public Collection<ChessMove> getQueenMoves() {
        return ValidMovesCalculator;
    }
}