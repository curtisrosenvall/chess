package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Objects;

public class BishopMovesCalculator {

    Collection<ChessMove> validMovesCalculator;

    public BishopMovesCalculator(ChessBoard board, ChessPosition startPosition) {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BishopMovesCalculator that = (BishopMovesCalculator) o;
        return Objects.equals(validMovesCalculator, that.validMovesCalculator);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(validMovesCalculator);
    }

    @Override
    public String toString() {
        String output = "BishopValidMoves{" +
                "validMoves= ";
        for (ChessMove move : validMovesCalculator) {
            output += "{ " + move.getStartPosition() + "," + move.getEndPosition() + "} ";
        }
        return output;
    }
}