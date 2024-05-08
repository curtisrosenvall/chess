package chess;

import java.util.Collection;
import java.util.ArrayList;

public class KnightMovesCalculator {

    Collection<ChessMove> ValidMovesCalculator;

    public KnightMovesCalculator(ChessBoard board, ChessPosition startPosition) {

        ValidMovesCalculator = new ArrayList<>();
        ValidMovesCalculator testMove = new ValidMovesCalculator();

        //2 -> 1
        ChessPosition endPosition = new ChessPosition(startPosition.getRow() + 2, startPosition.getColumn() + 1);
        if((endPosition.getRow() <= 8) && (endPosition.getColumn() <= 8)) {
            testMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
        }

        //U 1 <- 2
        endPosition = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() - 2);
        if((endPosition.getRow() <= 8) && (endPosition.getColumn() >= 1)) {
            testMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
        }

        //D 1 -> 2
        endPosition = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() + 2);
        if((endPosition.getRow() >= 1) && (endPosition.getColumn() <= 8)) {
            testMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
        }

        //2 -> 2
        endPosition = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 2);
        if((endPosition.getRow() <= 8) && (endPosition.getColumn() <= 8)) {
            testMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
        }


        //D 2 -> 1
        endPosition  = new ChessPosition(startPosition.getRow() - 2, startPosition.getColumn() + 1);
        if((endPosition.getRow() >= 1) && (endPosition.getColumn() <= 8)) {
            testMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
        }

        //U 2 <- 1
        endPosition = new ChessPosition(startPosition.getRow() + 2, startPosition.getColumn() - 1);
        if((endPosition.getRow() <= 8) && (endPosition.getColumn() >= 1)) {
            testMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
        }

        //D 2 <- 1
        endPosition = new ChessPosition(startPosition.getRow() - 2, startPosition.getColumn() - 1);
        if((endPosition.getRow() >= 1) && (endPosition.getColumn() >=1)) {
            testMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
        }

        //D 1 <- 2
        endPosition = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() - 2);
        if((endPosition.getRow() >= 1) && (endPosition.getColumn() >= 1)) {
            testMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
        }




    }

    public Collection<ChessMove> getKnightMoves() {
        return ValidMovesCalculator;
    }
}