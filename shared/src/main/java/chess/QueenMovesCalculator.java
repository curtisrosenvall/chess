package chess;

import java.util.Collection;
import java.util.ArrayList;

public class QueenMovesCalculator {

    Collection<ChessMove> ValidMovesCalculator;

    public QueenMovesCalculator(ChessBoard board, ChessPosition startPosition) {

        ValidMovesCalculator = new ArrayList<>();
        ValidMovesCalculator validMove = new ValidMovesCalculator();

        //^
        boolean validPosition = true;
        int i = 1;
        while(validPosition) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() + i, startPosition.getColumn());
            if (endPosition.getRow() > 8) {
                validPosition = false;
            } else {
                validPosition = validMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
            }
            i++;
        }

        //->
        validPosition = true;
        i = 1;
        while(validPosition) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow(), startPosition.getColumn() + i);
            if (endPosition.getColumn() > 8) {
                validPosition = false;
            } else {
                validPosition = validMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
            }
            i++;
        }

        //^ ->
        validPosition = true;
        i = 1;
        while(validPosition) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() + i, startPosition.getColumn() + i);
            if ((endPosition.getRow() > 8) || (endPosition.getColumn() > 8)) {
                validPosition = false;
            } else {
                validPosition = validMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
            }
            i++;
        }



        //D ->
        validPosition = true;
        i = 1;
        while(validPosition) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() - i, startPosition.getColumn() + i);
            if ((endPosition.getRow() < 1) || (endPosition.getColumn() > 8)) {
                validPosition = false;
            } else {
                validPosition = validMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
            }
            i++;
        }

        //^ ->
        validPosition = true;
        i = 1;
        while(validPosition) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() + i, startPosition.getColumn() - i);
            if((endPosition.getRow() > 8) || (endPosition.getColumn() < 1)) {
                validPosition = false;
            } else {
                validPosition = validMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
            }
            i++;
        }

        //D
        validPosition = true;
        i = 1;
        while(validPosition) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() - i, startPosition.getColumn());
            if (endPosition.getRow() < 1) {
                validPosition = false;
            } else {
                validPosition = validMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
            }
            i++;
        }

        //D <-
        validPosition = true;
        i = 1;
        while(validPosition) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() - i, startPosition.getColumn() - i);
            if ((endPosition.getRow() < 1) || (endPosition.getColumn() < 1)) {
                validPosition = false;
            } else {
                validPosition = validMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
            }
            i++;
        }

        //<-
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


    }

    public Collection<ChessMove> getQueenMoves() {
        return ValidMovesCalculator;
    }
}