package chess;

import java.util.Collection;
import java.util.ArrayList;

/**
 * Calculates valid moves for a Rook on a chessboard.
 */
public class CalculateRookMoves {

    Collection<ChessMove> validMovesCalculator;

    public CalculateRookMoves(ChessBoard board, ChessPosition startPosition) {

        validMovesCalculator = new ArrayList<>();
        ValidMovesCalculator validMove = new ValidMovesCalculator();
        ChessPiece rook = board.getPiece(startPosition);

        // Validate that there is a rook at the start position
        if (rook == null || rook.getPieceType() != ChessPiece.PieceType.ROOK) {
            return; // No rook to move
        }

        // Up, Down, Right, Left
        int[][] directions = {
                {1, 0},   // Up
                {-1, 0},  // Down
                {0, 1},   // Right
                {0, -1}   // Left
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

                // Attempt to move the rook to the endPosition
                boolean canContinue = validMove.movePiece(validMovesCalculator, board, startPosition, endPosition);

                if (!canContinue) {
                    break; // Move is blocked by a piece; cannot continue in this direction
                }

                step++;
            }
        }
    }

    /**
     * Retrieves the collection of valid Rook moves.
     *
     * @return A collection of valid ChessMove objects for the Rook.
     */
    public Collection<ChessMove> getRookMoves() {
        return validMovesCalculator;
    }
}