package chess;
import chess.pieces.PawnMovement;
import chess.pieces.PieceMovement;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece.
 * Note: You can add to this class, but you may not alter the signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor teamColor;
    private PieceType type;

    // Constructor
    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.type = type;
        this.teamColor = getTeamColor();
    }



    /**
     * Enum representing the different types of chess pieces.
     */
    public enum PieceType {
        KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN
    }



    /**
     * Returns the team color of this chess piece.
     */
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    /**
     * Returns the type of this chess piece.
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to.
     * Does not take into account moves that are illegal due to leaving the king in danger.
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        return new ArrayList<>();
    }
}
