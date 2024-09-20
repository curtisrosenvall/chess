package chess;

import java.util.Collection;
import java.util.ArrayList;

public class CalculateKnightMoves implements MovesCalculator {

    private Collection<ChessMove> validMoves;


    public CalculateKnightMoves(ChessBoard board, ChessPosition startPosition) {
        validMoves = new ArrayList<>();

        ChessPiece knight = board.getPiece(startPosition);
        if (knight == null || knight.getPieceType() != ChessPiece.PieceType.KNIGHT) {
            return; // No Knight at the start position
        }

        // Define all 8 possible Knight move offsets
        int[][] moveOffsets = {
                {2, 1}, {1, 2}, {-1, 2}, {-2, 1},
                {-2, -1}, {-1, -2}, {1, -2}, {2, -1}
        };

        // Iterate through each possible move
        for (int[] offset : moveOffsets) {
            int newRow = startPosition.getRow() + offset[0];
            int newCol = startPosition.getColumn() + offset[1];
            ChessPosition endPosition = new ChessPosition(newRow, newCol);

            // Check if the end position is within the board and handle the move
            ValidMovesCalculator.checkBoardLimitsAndMove(validMoves, board, startPosition, endPosition);
        }
    }

    @Override
    public Collection<ChessMove> getValidMoves() {
        return validMoves;
    }
}