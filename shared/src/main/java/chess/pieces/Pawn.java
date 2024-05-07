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
        int direction = this.getTeamColor() == ChessGame.TeamColor.WHITE ? 1 : -1;
        int startRow = this.getTeamColor() == ChessGame.TeamColor.WHITE ? 1 : 6;
        int promotionRow = this.getTeamColor() == ChessGame.TeamColor.WHITE ? 7 : 0;

        // Forward move
        ChessPosition forwardPos = new ChessPosition(position.getRow() + direction, position.getColumn());
        if (board.isPositionValid(forwardPos) && board.getPiece(forwardPos) == null) {
            moves.add(new ChessMove(position, position, this.getPieceType()));

            // Pawn promotion logic (optional)
            if (forwardPos.getRow() == promotionRow) {
                // You may want to handle pawn promotion here
                // For simplicity, let's promote it to a queen
                moves.add(new ChessMove(position, position, PieceType.QUEEN));
                // You can add promotion to other pieces like Knight, Bishop, Rook too
                // Remember, this is just a basic implementation
            }
        }

        // Capture moves (to be implemented)
        // Add logic for capturing opponent's pieces diagonally

        // Other logic like en passant, double move from starting position can be added here

        return moves;
    }
}
