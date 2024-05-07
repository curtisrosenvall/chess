package chess;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;


/**
 * Represents a single chess piece.
 * Note: You can add to this class, but you may not alter the signature of the existing methods.
 */
public class ChessPiece {
    protected PieceType type;
    protected ChessGame.TeamColor color;


    // Constructor
    public ChessPiece(ChessGame.TeamColor color, PieceType type) {
        this.type = type;
        this.color = color;

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
        return color;
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
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();

        // Implement the logic to calculate valid moves based on the specific piece type
        // Each chess piece has its own movement rules

        return moves;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return type == that.type && color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, color);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "type=" + type +
                ", color=" + color +
                '}';
    }
}
