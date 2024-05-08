package chess;

import java.util.Collection;
import java.util.ArrayList;

public class KingMovesCalculator {

    Collection<ChessMove> ValidMovesCalculator;

    public KingMovesCalculator(ChessBoard board, ChessPosition position) {

        ValidMovesCalculator = new ArrayList<>();
        ValidMovesCalculator testMove = new ValidMovesCalculator();

        //^
        ChessPosition endPosition = new ChessPosition(position.getRow() + 1, position.getColumn());
        if(endPosition.getRow() <= 8)
            testMove.movePiece(ValidMovesCalculator, board, position, endPosition);


        //->
        endPosition = new ChessPosition(position.getRow(), position.getColumn() + 1);
        if(endPosition.getColumn() <=8)
            testMove.movePiece(ValidMovesCalculator, board, position, endPosition);

        //<
        endPosition = new ChessPosition(position.getRow(), position.getColumn() - 1);
        if(endPosition.getColumn() >= 1)
            testMove.movePiece(ValidMovesCalculator, board, position, endPosition);

        //D ->
        endPosition = new ChessPosition(position.getRow() - 1, position.getColumn() + 1);
        if((endPosition.getRow() >= 1) && (endPosition.getColumn() <= 8))
            testMove.movePiece(ValidMovesCalculator, board, position, endPosition);

        //^ ->
        endPosition = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
        if((endPosition.getRow() <= 8) && (endPosition.getColumn() >= 1))
            testMove.movePiece(ValidMovesCalculator, board, position, endPosition);

        //^ <-
        endPosition = new ChessPosition(position.getRow() + 1, position.getColumn() - 1);
        if((endPosition.getRow() <= 8) && (endPosition.getColumn() >= 1))
            testMove.movePiece(ValidMovesCalculator, board, position, endPosition);

        //D
        endPosition = new ChessPosition(position.getRow() - 1, position.getColumn());
        if(endPosition.getRow() >= 1)
            testMove.movePiece(ValidMovesCalculator, board, position, endPosition);

        //D <-
        endPosition = new ChessPosition(position.getRow() - 1, position.getColumn() - 1);
        if((endPosition.getRow() >= 1) && (endPosition.getColumn() >= 1))
            testMove.movePiece(ValidMovesCalculator, board, position, endPosition);




    }

    public Collection<ChessMove> getKingMoves () {
        return ValidMovesCalculator;
    }
}