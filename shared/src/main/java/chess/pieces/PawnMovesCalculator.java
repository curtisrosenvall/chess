package chess.pieces;

import chess.*;
import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        int direction = board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE ? 1 : -1;
        int startRow = board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE ? 1 : 6;
        int promotionRow = board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE ? 7 : 0;

        // Single forward move
        ChessPosition forwardOne = new ChessPosition(position.getRow() + direction, position.getColumn());
        if (board.isPositionValid(forwardOne) && board.getPiece(forwardOne) == null) {
            addMoveIfPromotion(board, position, forwardOne, promotionRow, moves);
        }

        // Initial double forward move
        if (position.getRow() == startRow) {
            ChessPosition forwardTwo = new ChessPosition(position.getRow() + 2 * direction, position.getColumn());
            if (board.isPositionValid(forwardTwo) && board.getPiece(forwardTwo) == null && board.getPiece(forwardOne) == null) {
                moves.add(new ChessMove(position, forwardTwo, ChessPiece.PieceType.PAWN));
            }
        }

        // Capture moves
        int[] captureOffsets = {-1, 1};
        for (int offset : captureOffsets) {
            ChessPosition capturePos = new ChessPosition(position.getRow() + direction, position.getColumn() + offset);
            if (board.isPositionValid(capturePos) && board.getPiece(capturePos) != null &&
                    board.getPiece(capturePos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                addMoveIfPromotion(board, position, capturePos, promotionRow, moves);
            }
        }

        return moves;
    }

    private void addMoveIfPromotion(ChessBoard board, ChessPosition start, ChessPosition end, int promotionRow, Collection<ChessMove> moves) {
        if (end.getRow() == promotionRow) {
            // Handle promotion; this might involve creating a new type of ChessMove that includes the promotion piece type
            moves.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN)); // Example: Always promotes to Queen
        } else {
            moves.add(new ChessMove(start, end, ChessPiece.PieceType.PAWN));
        }
    }
}
