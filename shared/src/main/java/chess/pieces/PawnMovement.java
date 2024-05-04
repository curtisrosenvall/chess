package chess.pieces;

import chess.*;
import chess.ChessPiece.PieceType;
import java.util.ArrayList;
import java.util.Collection;

public class PawnMovement implements PieceMovement {

    @Override
    public Collection<ChessMove> getPossibleMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(position);
        int direction = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? 1 : -1;

        // Single move forward
        ChessPosition oneStep = position.translate(direction, 0);
        if (board.isPositionEmpty(oneStep)) {
            if (oneStep.getRow() == 1 || oneStep.getRow() == 8) { // Promotion row for either color
                addPromotionMoves(position, oneStep, moves);
            } else {
                moves.add(new ChessMove(position, oneStep, null));
            }

            // Double move from initial position
            if ((piece.getTeamColor() == ChessGame.TeamColor.WHITE && position.getRow() == 2) ||
                    (piece.getTeamColor() == ChessGame.TeamColor.BLACK && position.getRow() == 7)) {
                ChessPosition twoSteps = position.translate(2 * direction, 0);
                if (board.isPositionEmpty(twoSteps)) {
                    moves.add(new ChessMove(position, twoSteps, null));
                }
            }
        }

        // Capture moves
        ChessPosition[] captures = {
                position.translate(direction, -1),
                position.translate(direction, 1)
        };
        for (ChessPosition capture : captures) {
            if (board.isPositionOccupiedByOpponent(capture, piece.getTeamColor())) {
                if (capture.getRow() == 1 || capture.getRow() == 8) { // Promotion row
                    addPromotionMoves(position, capture, moves);
                } else {
                    moves.add(new ChessMove(position, capture, null));
                }
            }
        }

        return moves;
    }

    private void addPromotionMoves(ChessPosition start, ChessPosition end, Collection<ChessMove> moves) {
        moves.add(new ChessMove(start, end, PieceType.QUEEN));
        moves.add(new ChessMove(start, end, PieceType.ROOK));
        moves.add(new ChessMove(start, end, PieceType.BISHOP));
        moves.add(new ChessMove(start, end, PieceType.KNIGHT));
    }
}
