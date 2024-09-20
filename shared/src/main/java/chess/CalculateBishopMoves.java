package chess;

import java.util.Collection;
import java.util.ArrayList;

public class CalculateBishopMoves {

    Collection<ChessMove> validMovesCalculator;

    public CalculateBishopMoves(ChessBoard board, ChessPosition startPosition) {

        validMovesCalculator = new ArrayList<>();
        ValidMovesCalculator validMove = new ValidMovesCalculator();

        int[][] directions = { {1, 1}, {-1, 1}, {-1, -1}, {1, -1} };

        for (int[] direction : directions) {
            boolean validPosition = true;
            int i = 1;
            while(validPosition) {
                ChessPosition endPosition = new ChessPosition(
                        startPosition.getRow() + i * direction[0],
                        startPosition.getColumn() + i * direction[1]
                );
                validPosition = validMove.checkBoardLimits(validMovesCalculator, board, startPosition, endPosition);
                i++;
            }
        }
    }

    public Collection<ChessMove> getBishopMoves() {
        return validMovesCalculator;
    }
}