package chess;

import java.util.Collection;
import java.util.ArrayList;

public class CalculateQueenMoves implements MovesCalculator {

    private Collection<ChessMove> validMoves;

    public CalculateQueenMoves(ChessBoard board, ChessPosition startPosition) {
        validMoves = new ArrayList<>();

        ChessPiece queen = board.getPiece(startPosition);
        if (queen == null || queen.getPieceType() != ChessPiece.PieceType.QUEEN) {
            return; // No queen at the start position
        }

        // Define the eight directions the queen can move
        int[] rowDirections   = { 1,  1,  0, -1, -1, -1,  0,  1};
        int[] colDirections   = { 0,  1,  1,  1,  0, -1, -1, -1};

        for (int dir = 0; dir < 8; dir++) {
            int rowOffset = rowDirections[dir];
            int colOffset = colDirections[dir];
            int steps = 1;

            while (true) {
                int newRow = startPosition.getRow() + steps * rowOffset;
                int newCol = startPosition.getColumn() + steps * colOffset;
                ChessPosition endPosition = new ChessPosition(newRow, newCol);

                if (!isInBoard(endPosition)) {
                    break; // Move is off the board
                }

                ChessPiece targetPiece = board.getPiece(endPosition);
                if (targetPiece == null) {
                    // Empty square, add move and continue
                    validMoves.add(new ChessMove(startPosition, endPosition, null));
                } else if (targetPiece.getTeamColor() != queen.getTeamColor()) {
                    // Capture opponent's piece, add move and stop in this direction
                    validMoves.add(new ChessMove(startPosition, endPosition, null));
                    break;
                } else {
                    // Friendly piece blocks the path, stop in this direction
                    break;
                }
                steps++;
            }
        }
    }

    @Override
    public Collection<ChessMove> getValidMoves() {
        return validMoves;
    }

    private boolean isInBoard(ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }
}