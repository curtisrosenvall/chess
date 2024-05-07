package passoff.chess.piece;
import chess.pieces.Pawn;

import chess.*;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import static org.junit.jupiter.api.Assertions.*;

class PawnTest {

    @Test
    public void pawnMiddleOfBoardWhite() {
        ChessBoard board = new ChessBoard();
        ChessPosition pawnPosition = new ChessPosition(4, 4);
        board.addPiece(pawnPosition, new Pawn(ChessGame.TeamColor.WHITE)); // Place a white pawn at (4, 4)

        Collection<ChessMove> moves = board.getPiece(pawnPosition).pieceMoves(board, pawnPosition);

        // Expected move: one step forward from (4, 4) to (5, 4)
        ChessMove expectedMove = new ChessMove(pawnPosition, new ChessPosition(5, 4), ChessPiece.PieceType.PAWN);

        // Assert that the moves contain the expected move
        assertTrue(moves.contains(expectedMove), "Expected move should contain moving one step forward");
    }
}
