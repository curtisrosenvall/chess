package chess;
import java.util.Collection;
import java.util.ArrayList;


public class CalculateKnightMoves {

    Collection<ChessMove> validMovesCalculator;

    public CalculateKnightMoves(ChessBoard board, ChessPosition startPosition) {

        validMovesCalculator = new ArrayList<>();
        ValidMovesCalculator validMove = new ValidMovesCalculator();

        int[][] moveOffsets = {
                {2, 1}, {1, 2}, {-1, 2}, {-2, 1},
                {-2, -1}, {-1, -2}, {1, -2}, {2, -1}
        };

        for (int[] offset : moveOffsets) {
            ChessPosition endPosition = new ChessPosition(
                    startPosition.getRow() + offset[0],
                    startPosition.getColumn() + offset[1]
            );

            if (validMove.isInBoard(endPosition)) {
                validMove.movePiece(validMovesCalculator, board, startPosition, endPosition);
            }
        }
    }

    public Collection<ChessMove> getKnightMoves() {
        return validMovesCalculator;
    }
}