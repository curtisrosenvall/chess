package chess;

import java.util.Collection;
import java.util.ArrayList;

public class KnightMovesCalculator {

    Collection<ChessMove> ValidMovesCalculator;

    public KnightMovesCalculator(ChessBoard board, ChessPosition startPosition) {

        ValidMovesCalculator = new ArrayList<>();
        ValidMovesCalculator validMove = new ValidMovesCalculator();

        //2 -> 1
        ChessPosition endPosition = new ChessPosition(startPosition.getRow() + 2, startPosition.getColumn() + 1);
        if((endPosition.getRow() <= 8) && (endPosition.getColumn() <= 8)) {
            validMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
        }

        //U 1 <- 2
        endPosition = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() - 2);
        if((endPosition.getRow() <= 8) && (endPosition.getColumn() >= 1)) {
            validMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
        }

        //D 1 -> 2
        endPosition = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() + 2);
        if((endPosition.getRow() >= 1) && (endPosition.getColumn() <= 8)) {
            validMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
        }

        //up 1 right 2
        endPosition = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 2);
        if((endPosition.getRow() <= 8) && (endPosition.getColumn() <= 8)) {
            validMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
        }


        //D 2 -> 1
        endPosition  = new ChessPosition(startPosition.getRow() - 2, startPosition.getColumn() + 1);
        if((endPosition.getRow() >= 1) && (endPosition.getColumn() <= 8)) {
            validMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
        }

        //U 2 <- 1
        endPosition = new ChessPosition(startPosition.getRow() + 2, startPosition.getColumn() - 1);
        if((endPosition.getRow() <= 8) && (endPosition.getColumn() >= 1)) {
            validMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
        }

        //D 2 <- 1
        endPosition = new ChessPosition(startPosition.getRow() - 2, startPosition.getColumn() - 1);
        if((endPosition.getRow() >= 1) && (endPosition.getColumn() >=1)) {
            validMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
        }

        //D 1 <- 2
        endPosition = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() - 2);
        if((endPosition.getRow() >= 1) && (endPosition.getColumn() >= 1)) {
            validMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);
        }




    }

    public Collection<ChessMove> getKnightMoves() {
        return ValidMovesCalculator;
    }
}