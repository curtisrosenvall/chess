package chess;


import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard implements Cloneable{
    private ChessPiece[][] squares = new ChessPiece[8][8];
    private ChessMove lastMove;

    public ChessBoard() {
//        nothing for now
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() -1][position.getColumn() -1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() -1 ][position.getColumn() -1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
            // Define the order of pieces on the back rank
            ChessPiece.PieceType[] pieceBoardOrder = {
                    ChessPiece.PieceType.ROOK,
                    ChessPiece.PieceType.KNIGHT,
                    ChessPiece.PieceType.BISHOP,
                    ChessPiece.PieceType.QUEEN,
                    ChessPiece.PieceType.KING,
                    ChessPiece.PieceType.BISHOP,
                    ChessPiece.PieceType.KNIGHT,
                    ChessPiece.PieceType.ROOK
            };
            // Place white pieces
            for (int i = 0; i < 8; i++) {
                // Place back rank pieces
                addPiece(new ChessPosition(1, i + 1), new ChessPiece(ChessGame.TeamColor.WHITE, pieceBoardOrder[i]));
                // Place pawns
                addPiece(new ChessPosition(2, i + 1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            }
            // Place black pieces
            for (int i = 0; i < 8; i++) {
                // Place back rank pieces
                addPiece(new ChessPosition(8, i + 1), new ChessPiece(ChessGame.TeamColor.BLACK, pieceBoardOrder[i]));
                // Place pawns
                addPiece(new ChessPosition(7, i + 1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
            }
        }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "squares=" + Arrays.toString(squares) +
                '}';
    }

    /**
     * Creates and returns a deep copy of this ChessBoard.
     * @return A new ChessBoard object with the same piece arrangement as this one.
     * @throws CloneNotSupportedException if the ChessPiece objects in the board do not support cloning.
     */

    @Override
    protected ChessBoard clone() throws CloneNotSupportedException {
        ChessBoard clone = (ChessBoard) super.clone();
        ChessPiece[][] clonedBoard = new ChessPiece[8][8];
        for(int i = 1; i < 9; i++) {
            for(int j = 1; j < 9; j++) {
                if(getPiece(new ChessPosition(i,j)) != null) {
                    clonedBoard[i-1][j-1] = (ChessPiece) getPiece(new ChessPosition(i,j)).clone();
                }
            }
        }
        clone.squares = clonedBoard;
        return clone;
    }

    /**
     * Retrieves the last move made on this chessboard.
     * @return The most recent ChessMove made.
     */

    public ChessMove getLastMove() {
        return lastMove;
    }

    /**
     * Sets the last move made on this chessboard.
     * @param lastMove The move to record as the last move.
     */

    public void setLastMove(ChessMove lastMove) {
        this.lastMove = lastMove;
    }

    public ChessPiece[][] getBoard() {
        return squares;
    }
}




