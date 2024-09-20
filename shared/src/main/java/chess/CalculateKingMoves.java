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

        int[] rowOffsets = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] colOffsets = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < rowOffsets.length; i++) {
            ChessPosition endPosition = new ChessPosition(
                    startPosition.getRow() + rowOffsets[i],
                    startPosition.getColumn() + colOffsets[i]
            );
            ValidMovesCalculator.checkBoardLimitsAndMove(validMoves, board, startPosition, endPosition);
        }
    }

    @Override
    public Collection<ChessMove> getValidMoves() {
        return validMoves;
    }
}