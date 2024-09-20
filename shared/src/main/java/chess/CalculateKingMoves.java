package chess;

import java.util.Collection;
import java.util.ArrayList;

public class CalculateKingMoves implements MovesCalculator {

    private Collection<ChessMove> validMoves;

    public CalculateKingMoves(ChessBoard board, ChessPosition startPosition) {
        validMoves = new ArrayList<>();

        ChessPiece king = board.getPiece(startPosition);
        if (king == null || king.getPieceType() != ChessPiece.PieceType.KING) {
            return; // No king at the start position
        }

        // Possible moves for the king: one square in any direction
        int[] rowOffsets = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] colOffsets = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < rowOffsets.length; i++) {
            int newRow = startPosition.getRow() + rowOffsets[i];
            int newCol = startPosition.getColumn() + colOffsets[i];
            ChessPosition endPosition = new ChessPosition(newRow, newCol);

            if (isInBoard(endPosition)) {
                ChessPiece targetPiece = board.getPiece(endPosition);
                if (targetPiece == null || targetPiece.getTeamColor() != king.getTeamColor()) {
                    validMoves.add(new ChessMove(startPosition, endPosition, null));
                }
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