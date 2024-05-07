package chess.pieces;

import chess.*;
import java.util.ArrayList;
import java.util.Collection;

public class Queen extends ChessPiece {

    public Queen(ChessGame.TeamColor color) {
        super(color, PieceType.QUEEN);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        System.out.println("Calculating moves for Queen at: " + position);
        Collection<ChessMove> moves = new ArrayList<>();
        int[] directions = {-1, 0, 1};
        for (int dx : directions) {
            for (int dy : directions) {
                if (dx == 0 && dy == 0) continue;
                int nextRow = position.getRow() + dx;
                int nextCol = position.getColumn() + dy;
                while (nextRow >= 0 && nextRow < 8 && nextCol >= 0 && nextCol < 8) {
                    ChessPosition nextPos = new ChessPosition(nextRow, nextCol);
                    if (board.getPiece(nextPos) == null) {
                        moves.add(new ChessMove(position, nextPos, this.type));
                        System.out.println("Adding move to: " + nextPos);
                    } else {
                        if (board.getPiece(nextPos).getTeamColor() != this.color) {
                            moves.add(new ChessMove(position, nextPos, this.type));
                            System.out.println("Adding capture move to: " + nextPos);
                        }
                        break;
                    }
                    nextRow += dx;
                    nextCol += dy;
                }
            }
        }
        return moves;
    }
}

