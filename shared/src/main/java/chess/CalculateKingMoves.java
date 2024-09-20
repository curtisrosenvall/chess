package chess;

import java.util.Collection;
import java.util.ArrayList;

/**
 * Calculates valid moves for a King on a chessboard.
 */
public class CalculateKingMoves {

    Collection<ChessMove> validMovesCalculator;

    public CalculateKingMoves(ChessBoard board, ChessPosition startPosition) {

        validMovesCalculator = new ArrayList<>();
        ValidMovesCalculator validMove = new ValidMovesCalculator();

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
            ChessPosition endPosition = new ChessPosition(
                    startPosition.getRow() + direction[0],
                    startPosition.getColumn() + direction[1]
            );

            if (validMove.isInBoard(endPosition)) {
                validMove.movePiece(validMovesCalculator, board, startPosition, endPosition);
            }
        }
    }

    /**
     * Retrieves the collection of valid King moves.
     *
     * @return A collection of valid ChessMove objects for the King.
     */
    public Collection<ChessMove> getKingMoves() {
        return validMovesCalculator;
    }
}