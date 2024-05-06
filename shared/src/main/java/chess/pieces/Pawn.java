package chess.pieces;
import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class Pawn extends ChessPiece {

    public Pawn(ChessGame.TeamColor color) {
        super(color, PieceType.PAWN);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece pawn = board.getPiece(position);
        if (pawn == null) {
            return moves; // Early exit if no pawn at the given position
        }

        int direction = pawn.getTeamColor() == ChessGame.TeamColor.WHITE ? 1 : -1;
        int startRow = pawn.getTeamColor() == ChessGame.TeamColor.WHITE ? 1 : 6;
        int promotionRow = pawn.getTeamColor() == ChessGame.TeamColor.WHITE ? 7 : 0;

        // Normal move
        ChessPosition oneStep = new ChessPosition(position.getRow() + direction, position.getColumn());
        if (board.isPositionValid(oneStep) && board.getPiece(oneStep) == null) {
            handlePromotion(position, oneStep, promotionRow, moves, pawn);
        }

        // Double move from starting position
        if (position.getRow() == startRow) {
            ChessPosition twoSteps = new ChessPosition(position.getRow() + 2 * direction, position.getColumn());
            if (board.isPositionValid(twoSteps) && board.getPiece(twoSteps) == null && board.getPiece(oneStep) == null) {
                moves.add(new ChessMove(position, twoSteps, pawn.getPieceType()));
            }
        }

        // Capture moves
        int[] cols = {-1, 1};
        for (int col : cols) {
            ChessPosition diag = new ChessPosition(position.getRow() + direction, position.getColumn() + col);
            if (board.isPositionValid(diag) && board.getPiece(diag) != null &&
                    board.getPiece(diag).getTeamColor() != pawn.getTeamColor()) {
                handlePromotion(position, diag, promotionRow, moves, pawn);
            }
        }

        return moves;
    }

    private void handlePromotion(ChessPosition start, ChessPosition end, int promotionRow, Collection<ChessMove> moves, ChessPiece pawn) {
        if (end.getRow() == promotionRow) {
            moves.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN));  // Simulated promotion to queen
        } else {
            moves.add(new ChessMove(start, end, pawn.getPieceType()));
        }
    }
}
