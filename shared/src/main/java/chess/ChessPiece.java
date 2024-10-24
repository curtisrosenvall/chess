package chess;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece implements Cloneable{

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
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
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition startPosition) {
        Collection<ChessMove> validMovesCalculator = null;

        ChessPiece piece = board.getPiece(startPosition);


        switch (piece.getPieceType()) {
            case BISHOP:
                CalculateBishopMoves bishop = new CalculateBishopMoves(board, startPosition);
                validMovesCalculator = bishop.getBishopMoves();
                break;
            case ROOK:
                CalculateRookMoves rook = new CalculateRookMoves(board, startPosition);
                validMovesCalculator = rook.getRookMoves();
                break;
            case QUEEN:
                CalculateQueenMoves queen = new CalculateQueenMoves(board, startPosition);
                validMovesCalculator = queen.getQueenMoves();
                break;
            case KING:
                CalculateKingMoves king = new CalculateKingMoves(board, startPosition);
                validMovesCalculator = king.getKingMoves();
                break;
            case PAWN:
                CalculatePawnMoves pawn = new CalculatePawnMoves(board, startPosition);
                validMovesCalculator = pawn.getPawnMoves();
                break;
            case KNIGHT:
                CalculateKnightMoves knight = new CalculateKnightMoves(board, startPosition);
                validMovesCalculator = knight.getKnightMoves();
                break;
            default:
                validMovesCalculator = null;
                break;
        }

        return validMovesCalculator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
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

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
