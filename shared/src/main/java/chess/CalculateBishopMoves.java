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
            int rowOffset = direction[0];
            int colOffset = direction[1];
            int i = 1;
            while(true) {
                ChessPosition endPosition = new ChessPosition(
                        startPosition.getRow() + i * rowOffset,
                        startPosition.getColumn() + i * colOffset
                );

                if (!validMove.isInBoard(endPosition)) {
                    break;
                }

                boolean canContinue = validMove.movePiece(validMovesCalculator, board, startPosition, endPosition);

                if (!canContinue) {
                    break;
                }
                i++;
            }
        }
    }

    public Collection<ChessMove> getBishopMoves() {
        return validMovesCalculator;
    }
}