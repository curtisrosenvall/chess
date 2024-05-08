package chess;

import java.util.Collection;
import java.util.ArrayList;

public class KingMovesCalculator {

    Collection<ChessMove> ValidMovesCalculator;

    public KingMovesCalculator(ChessBoard board, ChessPosition startPosition) {

        ValidMovesCalculator = new ArrayList<>();
        ValidMovesCalculator validMove = new ValidMovesCalculator();

        //^
        ChessPosition endPosition = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn());
        if(endPosition.getRow() <= 8)
            validMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);


        //->
        endPosition = new ChessPosition(startPosition.getRow(), startPosition.getColumn() + 1);
        if(endPosition.getColumn() <=8)
            validMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);

        //<
        endPosition = new ChessPosition(startPosition.getRow(), startPosition.getColumn() - 1);
        if(endPosition.getColumn() >= 1)
            validMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);

        //D ->
        endPosition = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() + 1);
        if((endPosition.getRow() >= 1) && (endPosition.getColumn() <= 8))
            validMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);

        //^ ->
        endPosition = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 1);
        if((endPosition.getRow() <= 8) && (endPosition.getColumn() >= 1))
            validMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);

        //^ <-
        endPosition = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() - 1);
        if((endPosition.getRow() <= 8) && (endPosition.getColumn() >= 1))
            validMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);

        //D
        endPosition = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn());
        if(endPosition.getRow() >= 1)
            validMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);

        //D <-
        endPosition = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() - 1);
        if((endPosition.getRow() >= 1) && (endPosition.getColumn() >= 1))
            validMove.movePiece(ValidMovesCalculator, board, startPosition, endPosition);




    }

    public Collection<ChessMove> getKingMoves () {
        return ValidMovesCalculator;
    }
}