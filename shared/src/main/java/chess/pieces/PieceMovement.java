package chess.pieces;
import chess.ChessMove;
import chess.ChessBoard;
import chess.ChessPosition;

import java.util.Collection;

public interface PieceMovement {
    Collection<ChessMove> getPossibleMoves(ChessBoard board, ChessPosition position);
}
