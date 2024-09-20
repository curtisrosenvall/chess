package chess;

import java.util.Collection;
import java.util.ArrayList;

public class CalculateKingMoves {

    Collection<ChessMove> validMovesCalculator;

    public CalculateKingMoves(ChessBoard board, ChessPosition startPosition) {

        validMovesCalculator = new ArrayList<>();
        ValidMovesCalculator validMove = new ValidMovesCalculator();

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

    public Collection<ChessMove> getKingMoves() {
        return validMovesCalculator;
    }
}