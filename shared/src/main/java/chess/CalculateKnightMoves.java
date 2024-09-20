package chess;
import java.util.Collection;
import java.util.ArrayList;

/**
 * Calculates valid moves for a Knight on a chessboard.
 */
public class CalculateKnightMoves {

    Collection<ChessMove> validMovesCalculator;

    public CalculateKnightMoves(ChessBoard board, ChessPosition startPosition) {

        validMovesCalculator = new ArrayList<>();
        ValidMovesCalculator validMove = new ValidMovesCalculator();

        // Define all eight possible Knight move offsets
        int[][] moveOffsets = {
                {2, 1}, {1, 2}, {-1, 2}, {-2, 1},
                {-2, -1}, {-1, -2}, {1, -2}, {2, -1}
        };

        // Iterate through each possible move offset
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

    /**
     * Retrieves the collection of valid Knight moves.
     *
     * @return A collection of valid ChessMove objects for the Knight.
     */
    public Collection<ChessMove> getKnightMoves() {
        return validMovesCalculator;
    }
}