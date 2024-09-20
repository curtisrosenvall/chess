package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Objects;

public class CalculateBishopMoves {

    Collection<ChessMove> validMovesCalculator;

    public CalculateBishopMoves(ChessBoard board, ChessPosition startPosition) {

        validMovesCalculator = new ArrayList<>();
        ValidMovesCalculator validMove = new ValidMovesCalculator();

        //Up and to the right
        boolean validPosition = true;
        int i = 1;
        while(validPosition) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() + i, startPosition.getColumn() + i);
            validPosition = validMove.checkBoardLimits(validMovesCalculator, board, startPosition, endPosition);
            i++;
        }

        //Down and to the right
        validPosition = true;
        i = 1;
        while(validPosition) {
            ChessPosition enPosition = new ChessPosition(startPosition.getRow() - i, startPosition.getColumn() + i);
            validPosition = validMove.checkBoardLimits(validMovesCalculator, board, startPosition, enPosition);
            i++;
        }

        //Down and to the left
        validPosition = true;
        i = 1;
        while(validPosition) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() - i, startPosition.getColumn() - i);
            validPosition = validMove.checkBoardLimits(validMovesCalculator, board, startPosition, endPosition);
            i++;
        }

        //Up and to the left
        validPosition = true;
        i = 1;
        while(validPosition) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() + i, startPosition.getColumn() - i);
            validPosition = validMove.checkBoardLimits(validMovesCalculator, board, startPosition, endPosition);
            i++;
        }
    }

    public Collection<ChessMove> getBishopMoves() {
        return validMovesCalculator;
    }


}