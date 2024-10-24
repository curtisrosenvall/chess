package chess;

import java.util.Collection;
import java.util.ArrayList;

public class CalculateQueenMoves {

    Collection<ChessMove> validMovesCalculator;

    public CalculateQueenMoves(ChessBoard board, ChessPosition startPosition) {

        validMovesCalculator = new ArrayList<>();
        ValidMovesCalculator validMove = new ValidMovesCalculator();

        int[][] directions = {
                {1, 0},
                {1, 1},
                {0, 1},
                {-1, 1},
                {-1, 0},
                {-1, -1},
                {0, -1},
                {1, -1}
        };

        for (int[] direction : directions) {
            int rowOffset = direction[0];
            int colOffset = direction[1];
            int step = 1;

            while (true) {
                ChessPosition endPosition = new ChessPosition(
                        startPosition.getRow() + step * rowOffset,
                        startPosition.getColumn() + step * colOffset
                );


                if (!validMove.isInBoard(endPosition)) {
                    break;
                }


                boolean canContinue = validMove.movePiece(validMovesCalculator, board, startPosition, endPosition);

                if (!canContinue) {
                    break;
                }

                step++;
            }
        }
    }

    public Collection<ChessMove> getQueenMoves() {
        return validMovesCalculator;
    }
}