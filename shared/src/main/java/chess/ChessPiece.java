package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */

public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves*/
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {

        Collection<ChessMove> ValidMovesCalculator;
        ChessPiece piece = board.getPiece(position);

        if (piece.getPieceType() == PieceType.KNIGHT) {
            KnightMovesCalculator knight = new KnightMovesCalculator(board, position);
            ValidMovesCalculator = knight.getKnightMoves();
        } else if (piece.getPieceType() == PieceType.KING) {
            KingMovesCalculator king = new KingMovesCalculator(board, position);
            ValidMovesCalculator = king.getKingMoves();
        }  else if (piece.getPieceType() == PieceType.BISHOP) {
            BishopMovesCalculator bishop = new BishopMovesCalculator(board, position);
            ValidMovesCalculator = bishop.getBishopMoves();
        }  else if (piece.getPieceType() == PieceType.PAWN) {
            PawnMovesCalculator pawn = new PawnMovesCalculator(board, position);
            ValidMovesCalculator = pawn.getPawnMoves();
        }else if (piece.getPieceType() == PieceType.ROOK) {
            RookMovesCalculator rook = new RookMovesCalculator(board, position);
            ValidMovesCalculator = rook.getRookMoves();
        }else if (piece.getPieceType() == PieceType.QUEEN) {
            QueenMovesCalculator queen = new QueenMovesCalculator(board, position);
            ValidMovesCalculator = queen.getQueenMoves();
        }else {
            ValidMovesCalculator = null;
        }
        return ValidMovesCalculator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }
}