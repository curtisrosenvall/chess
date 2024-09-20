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
    private ChessPiece[][] squares = new ChessPiece[8][8];

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
        ChessGame.TeamColor whiteTeam = ChessGame.TeamColor.WHITE;
        ChessGame.TeamColor blackTeam = ChessGame.TeamColor.BLACK;

        ChessPiece.PieceType rook = ChessPiece.PieceType.ROOK;
        ChessPiece.PieceType knight = ChessPiece.PieceType.KNIGHT;
        ChessPiece.PieceType bishop = ChessPiece.PieceType.BISHOP;
        ChessPiece.PieceType queen = ChessPiece.PieceType.QUEEN;
        ChessPiece.PieceType king = ChessPiece.PieceType.KING;
        ChessPiece.PieceType pawn = ChessPiece.PieceType.PAWN;


        addPiece( new ChessPosition(1,1), new ChessPiece(whiteTeam, rook));
        addPiece( new ChessPosition(1,2), new ChessPiece(whiteTeam, knight));
        addPiece( new ChessPosition(1,3), new ChessPiece(whiteTeam, bishop ));
        addPiece( new ChessPosition(1,4), new ChessPiece(whiteTeam, queen));
        addPiece( new ChessPosition(1,5), new ChessPiece(whiteTeam, king));
        addPiece( new ChessPosition(1,6), new ChessPiece(whiteTeam, bishop));
        addPiece( new ChessPosition(1,7), new ChessPiece(whiteTeam, knight));
        addPiece( new ChessPosition(1,8), new ChessPiece(whiteTeam, rook));

        for(int i = 1; i <= 8; i++) {
            addPiece( new ChessPosition(2,i), new ChessPiece(whiteTeam, pawn));
        }

        addPiece( new ChessPosition(8,1), new ChessPiece(blackTeam, rook));
        addPiece( new ChessPosition(8,2), new ChessPiece(blackTeam, knight));
        addPiece( new ChessPosition(8,3), new ChessPiece(blackTeam, bishop));
        addPiece( new ChessPosition(8,4), new ChessPiece(blackTeam, queen));
        addPiece( new ChessPosition(8,5), new ChessPiece(blackTeam, king));
        addPiece( new ChessPosition(8,6), new ChessPiece(blackTeam, bishop));
        addPiece( new ChessPosition(8,7), new ChessPiece(blackTeam, knight));
        addPiece( new ChessPosition(8,8), new ChessPiece(blackTeam, rook));

        for(int i = 1; i <= 8; i++) {
            addPiece( new ChessPosition(7,i), new ChessPiece(blackTeam, pawn));
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
}
