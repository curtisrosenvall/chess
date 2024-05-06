package chess.pieces;
import chess.*;

import java.util.ArrayList;
import java.util.Collection;


public class Rook extends ChessPiece {
    public Rook(ChessGame.TeamColor color) {
        super(color, PieceType.ROOK);
    }
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        // Directions the Rook can move: vertical and horizontal
        int[][] directions = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

        // Get the rook piece at the given position to check its team color
        ChessPiece rook = board.getPiece(position);
        if (rook == null) return moves; // Return empty if no piece found (safety check)

        ChessGame.TeamColor rookColor = rook.getTeamColor(); // Get the team color of the rook

        for (int[] direction : directions) {
            ChessPosition current = position;
            while (true) {
                int newRow = current.getRow() + direction[0];
                int newCol = current.getColumn() + direction[1];
                ChessPosition nextPosition = new ChessPosition(newRow, newCol);

                if (!board.isPositionValid(nextPosition)) {
                    break;
                }

                ChessPiece pieceAtNextPosition = board.getPiece(nextPosition);
                if (pieceAtNextPosition == null) {
                    moves.add(new ChessMove(current, nextPosition, ChessPiece.PieceType.ROOK));
                } else {
                    if (pieceAtNextPosition.getTeamColor() != rookColor) {
                        moves.add(new ChessMove(current, nextPosition, ChessPiece.PieceType.ROOK));
                    }
                    break; // Stop if there is a piece (either to capture it, or blocked by it)
                }
                current = nextPosition; // Move to the next square in the direction
            }
        }
        return moves;
    }
}
