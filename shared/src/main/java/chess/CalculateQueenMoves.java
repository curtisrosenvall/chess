package chess;

import java.util.Collection;
import java.util.ArrayList;

/**
 * Calculates valid moves for a Queen on a chessboard.
 */
public class CalculateQueenMoves {

    Collection<ChessMove> validMovesCalculator;

    public CalculateQueenMoves(ChessBoard board, ChessPosition startPosition) {

        validMovesCalculator = new ArrayList<>();
        ValidMovesCalculator validMove = new ValidMovesCalculator();
        ChessPiece queen = board.getPiece(startPosition);

        if (queen == null || queen.getPieceType() != ChessPiece.PieceType.QUEEN) {
            return; // No queen to move
        }

        // Up, Up-Right, Right, Down-Right, Down, Down-Left, Left, Up-Left
        int[][] directions = {
                {1, 0},   // Up
                {1, 1},   // Up-Right
                {0, 1},   // Right
                {-1, 1},  // Down-Right
                {-1, 0},  // Down
                {-1, -1}, // Down-Left
                {0, -1},  // Left
                {1, -1}   // Up-Left
        };

        // Iterate through each direction and calculate valid moves
        for (int[] direction : directions) {
            int rowOffset = direction[0];
            int colOffset = direction[1];
            int step = 1;

            while (true) {
                ChessPosition endPosition = new ChessPosition(
                        startPosition.getRow() + step * rowOffset,
                        startPosition.getColumn() + step * colOffset
                );

                // Check if the new position is within the board limits
                if (!validMove.isInBoard(endPosition)) {
                    break; // Move is off the board
                }

                // Attempt to move the queen to the endPosition
                boolean canContinue = validMove.movePiece(validMovesCalculator, board, startPosition, endPosition);

                if (!canContinue) {
                    break; // Move is blocked by a piece; cannot continue in this direction
                }

                step++;
            }
        }
    }

    /**
     * Retrieves the collection of valid Queen moves.
     *
     * @return A collection of valid ChessMove objects for the Queen.
     */
    public Collection<ChessMove> getQueenMoves() {
        return validMovesCalculator;
    }
}