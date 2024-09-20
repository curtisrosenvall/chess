package chess;

import java.util.Collection;
import java.util.ArrayList;

public class CalculateKingMoves {

    Collection<ChessMove> validMovesCalculator;

    public CalculateKingMoves(ChessBoard board, ChessPosition startPosition) {

        validMovesCalculator = new ArrayList<>();
        ValidMovesCalculator validMove = new ValidMovesCalculator();

        //Up
        ChessPosition endPosition = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn());
        if(validMove.isInBoard(endPosition))
            validMove.movePiece(validMovesCalculator, board, startPosition, endPosition);

        //Up and to the right
        endPosition = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 1);
        if(validMove.isInBoard(endPosition))
            validMove.movePiece(validMovesCalculator, board, startPosition, endPosition);

        //Right
        endPosition = new ChessPosition(startPosition.getRow(), startPosition.getColumn() + 1);
        if(validMove.isInBoard(endPosition))
            validMove.movePiece(validMovesCalculator, board, startPosition, endPosition);

        //Down and Right
        endPosition = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() + 1);
        if(validMove.isInBoard(endPosition))
            validMove.movePiece(validMovesCalculator, board, startPosition, endPosition);

        //Down
        endPosition = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn());
        if(validMove.isInBoard(endPosition))
            validMove.movePiece(validMovesCalculator, board, startPosition, endPosition);

        //Down and Left
        endPosition = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() - 1);
        if(validMove.isInBoard(endPosition))
            validMove.movePiece(validMovesCalculator, board, startPosition, endPosition);

        //Left
        endPosition = new ChessPosition(startPosition.getRow(), startPosition.getColumn() - 1);
        if(validMove.isInBoard(endPosition))
            validMove.movePiece(validMovesCalculator, board, startPosition, endPosition);

        //Up and Left
        endPosition = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() - 1);
        if(validMove.isInBoard(endPosition))
            validMove.movePiece(validMovesCalculator, board, startPosition, endPosition);
    }

    public Collection<ChessMove> getKingMoves() {
        return validMovesCalculator;
    }
}