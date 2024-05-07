package chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class Knight extends ChessPiece {

    public Knight(ChessGame.TeamColor color) {
        super(color, PieceType.KNIGHT);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();

        // Define all possible knight moves relative to its current position
        int[][] possibleMoves = {
                {-2, -1}, {-2, 1}, {2, -1}, {2, 1},
                {-1, -2}, {-1, 2}, {1, -2}, {1, 2}
        };

        // Iterate through possible moves
        for (int[] move : possibleMoves) {
            int newRow = position.getRow() + move[0];
            int newCol = position.getColumn() + move[1];

            // Create a new position with the calculated row and column
            ChessPosition newPosition = new ChessPosition(newRow, newCol);

            // Check if the new position is valid and empty or has an opponent's piece
            if (board.isPositionValid(newPosition)) {
                ChessPiece pieceAtNewPosition = board.getPiece(newPosition);

                // If the position is empty or has an opponent's piece, add it to the list of valid moves
                if (pieceAtNewPosition == null || pieceAtNewPosition.getTeamColor() != this.getTeamColor()) {
                    moves.add(new ChessMove(position, newPosition, this.getPieceType()));
                }
            }
        }

        return moves;
    }
}


