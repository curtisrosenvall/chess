
package chess.pieces;
import chess.ChessBoard;
import chess.ChessPosition;
import chess.ChessMove;
import java.util.Collection;

public interface PieceMovesCalculator {
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position);
}
