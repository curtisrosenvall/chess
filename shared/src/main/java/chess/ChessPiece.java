package chess;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;


/**
 * Represents a single chess piece.
 * Note: You can add to this class, but you may not alter the signature of the existing methods.
 */
public class ChessPiece {
    protected ChessGame.TeamColor pieceColor;
    protected ChessPiece.PieceType type;


    // Constructor
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;

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
        return pieceColor;
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
        Collection<ChessMove> validMoves;

        ChessPiece piece = board.getPiece(position);

        if(piece.getPieceType() == PieceType.KING) {
            kingMovesCalculator kingMoves = new KingMovesCalculator(board, position);
            validmoves = kingMoves.getKingMoves();
        } else if (piece.getPieceType() == PieceType.QUEEN) {
            queenMovesCalculator queenMoves = new QueenMovesCalculator(board, position);
        } else if (piece.getPieceType() == PieceType.BISHOP) {
            bishopMovesCalculator bishopMoves = new BishopMovesCalculator(board, position);
        } else if (piece.getPieceType() == PieceType.KNIGHT) {
            knightMovesCalculator knightMoves = new KnightMovesCalculator(board,position);
        } else if (piece.getPieceType() == PieceType.ROOK) {
            rookMovesCalculator rookMoves = new RookMovesCalculator(board,position);
        }else if(piece.getPieceType() == PieceType.PAWN) {
            pawnMovesCalculator pawnMoves = new PawnMovesCalculator(board,position);
        }else {
            validMoves = null;
        }
        // Implement the logic to calculate valid moves based on the specific piece type
        // Each chess piece has its own movement rules

        return validmoves;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return type == that.type && pieceColor == that.pieceColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, pieceColor);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "type=" + type +
                ", color=" + pieceColor +
                '}';
    }
}
