package chess;

import java.util.Collection;
import java.util.ArrayList;

public class CalculateRookMoves {

    Collection<ChessMove> validMovesCalculator;

    public CalculateRookMoves(ChessBoard board, ChessPosition startPosition) {

        validMovesCalculator = new ArrayList<>();
        ValidMovesCalculator validMove = new ValidMovesCalculator();

        //Right
        boolean validPosition = true;
        int i = 1;
        while(validPosition) {
            ChessPosition endPosition = new ChessPosition( startPosition.getRow(), startPosition.getColumn() + i);
            validPosition = validMove.checkBoardLimits(validMovesCalculator, board, startPosition, endPosition);
            i++;
        }

        //Left
        validPosition = true;
        i = 1;
        while(validPosition) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow(), startPosition.getColumn() - i);
            validPosition = validMove.checkBoardLimits(validMovesCalculator, board, startPosition, endPosition);
            i++;
        }

        //Down
        validPosition = true;
        i = 1;
        while(validPosition) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() - i, startPosition.getColumn());
            validPosition = validMove.checkBoardLimits(validMovesCalculator, board, startPosition, endPosition);
            i++;
        }

        //Up
        validPosition = true;
        i = 1;
        while(validPosition) {
            ChessPosition endPosition = new ChessPosition( startPosition.getRow() + i, startPosition.getColumn());
            validPosition = validMove.checkBoardLimits(validMovesCalculator, board, startPosition, endPosition);
            i++;
        }
    }

    public Collection<ChessMove> getRookMoves() {
        return validMovesCalculator;
    }
}