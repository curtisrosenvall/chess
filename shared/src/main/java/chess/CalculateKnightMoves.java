package chess;

import java.util.Collection;
import java.util.ArrayList;

public class CalculateKnightMoves {

    Collection<ChessMove> validMovesCalculator;

    public CalculateKnightMoves(ChessBoard board, ChessPosition startPosition) {

        validMovesCalculator = new ArrayList<>();
        ValidMovesCalculator validMove = new ValidMovesCalculator();

        //Up two right one
        ChessPosition endPosition = new ChessPosition(startPosition.getRow() + 2, startPosition.getColumn() + 1);
        if(validMove.isInBoard(endPosition))
            validMove.movePiece(validMovesCalculator, board, startPosition, endPosition);

        //Up one right two
        endPosition = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 2);
        if(validMove.isInBoard(endPosition))
            validMove.movePiece(validMovesCalculator, board, startPosition, endPosition);

        //Down one right two
        endPosition = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() + 2);
        if(validMove.isInBoard(endPosition))
            validMove.movePiece(validMovesCalculator, board, startPosition, endPosition);

        //Down two right one
        endPosition  = new ChessPosition(startPosition.getRow() - 2, startPosition.getColumn() + 1);
        if(validMove.isInBoard(endPosition))
            validMove.movePiece(validMovesCalculator, board, startPosition, endPosition);

        //Down two left one
        endPosition = new ChessPosition(startPosition.getRow() - 2, startPosition.getColumn() - 1);
        if(validMove.isInBoard(endPosition))
            validMove.movePiece(validMovesCalculator, board, startPosition, endPosition);

        //Down one left two
        endPosition = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() - 2);
        if(validMove.isInBoard(endPosition))
            validMove.movePiece(validMovesCalculator, board, startPosition, endPosition);

        //Up one left two
        endPosition = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() - 2);
        if(validMove.isInBoard(endPosition))
            validMove.movePiece(validMovesCalculator, board, startPosition, endPosition);

        //Up two left one
        endPosition = new ChessPosition(startPosition.getRow() + 2, startPosition.getColumn() - 1);
        if(validMove.isInBoard(endPosition))
            validMove.movePiece(validMovesCalculator, board, startPosition, endPosition);
    }

    public Collection<ChessMove> getKnightMoves() {
        return validMovesCalculator;
    }
}
