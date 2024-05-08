package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] board = new ChessPiece[8][8];
    public ChessBoard() {

    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessGame.TeamColor teamWhite = ChessGame.TeamColor.WHITE;
        ChessGame.TeamColor teamBlack = ChessGame.TeamColor.BLACK;

        ChessPiece.PieceType typeRook = ChessPiece.PieceType.ROOK;
        ChessPiece.PieceType typeKnight = ChessPiece.PieceType.KNIGHT;
        ChessPiece.PieceType typeBishop = ChessPiece.PieceType.BISHOP;
        ChessPiece.PieceType typeQueen = ChessPiece.PieceType.QUEEN;
        ChessPiece.PieceType typeKing = ChessPiece.PieceType.KING;
        ChessPiece.PieceType typePawn = ChessPiece.PieceType.PAWN;


        addPiece( new ChessPosition(1,1), new ChessPiece(teamWhite, typeRook));
        addPiece( new ChessPosition(1,2), new ChessPiece(teamWhite, typeKnight));
        addPiece( new ChessPosition(1,3), new ChessPiece(teamWhite, typeBishop));
        addPiece( new ChessPosition(1,4), new ChessPiece(teamWhite, typeQueen));
        addPiece( new ChessPosition(1,5), new ChessPiece(teamWhite, typeKing));
        addPiece( new ChessPosition(1,6), new ChessPiece(teamWhite, typeBishop));
        addPiece( new ChessPosition(1,7), new ChessPiece(teamWhite, typeKnight));
        addPiece( new ChessPosition(1,8), new ChessPiece(teamWhite, typeRook));

        for(int i = 1; i <= 8; i++) {
            addPiece( new ChessPosition(2,i), new ChessPiece(teamWhite, typePawn));
        }

        addPiece( new ChessPosition(8,1), new ChessPiece(teamBlack, typeRook));
        addPiece( new ChessPosition(8,2), new ChessPiece(teamBlack, typeKnight));
        addPiece( new ChessPosition(8,3), new ChessPiece(teamBlack, typeBishop));
        addPiece( new ChessPosition(8,4), new ChessPiece(teamBlack, typeQueen));
        addPiece( new ChessPosition(8,5), new ChessPiece(teamBlack, typeKing));
        addPiece( new ChessPosition(8,6), new ChessPiece(teamBlack, typeBishop));
        addPiece( new ChessPosition(8,7), new ChessPiece(teamBlack, typeKnight));
        addPiece( new ChessPosition(8,8), new ChessPiece(teamBlack, typeRook));

        for(int i = 1; i <= 8; i++) {
            addPiece( new ChessPosition(7,i), new ChessPiece(teamBlack, typePawn));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
