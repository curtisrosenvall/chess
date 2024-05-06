package chess;
import chess.ChessGame.TeamColor;
import chess.pieces.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {
        //cool story

    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()][position.getColumn()];
    }

    // Checks if the position is within the board limits
    public boolean isPositionValid(ChessPosition position) {
        return position.getRow() >= 0 && position.getRow() < 8 &&
                position.getColumn() >= 0 && position.getColumn() < 8;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        // Initialize white pieces
        addPiece(new ChessPosition(0, 0), new Rook(TeamColor.WHITE));
        addPiece(new ChessPosition(0, 1), new Knight(TeamColor.WHITE));
        addPiece(new ChessPosition(0, 2), new Bishop(TeamColor.WHITE));
        addPiece(new ChessPosition(0, 3), new Queen(TeamColor.WHITE));
        addPiece(new ChessPosition(0, 4), new King(TeamColor.WHITE));
        addPiece(new ChessPosition(0, 5), new Bishop(TeamColor.WHITE));
        addPiece(new ChessPosition(0, 6), new Knight(TeamColor.WHITE));
        addPiece(new ChessPosition(0, 7), new Rook(TeamColor.WHITE));
        for (int i = 0; i < 8; i++) {
            addPiece(new ChessPosition(1, i), new Pawn(TeamColor.WHITE));
        }


        // Initialize black pieces
        addPiece(new ChessPosition(7, 0), new Rook(TeamColor.BLACK));
        addPiece(new ChessPosition(7, 1), new Knight(TeamColor.BLACK));
        addPiece(new ChessPosition(7, 2), new Bishop(TeamColor.BLACK));
        addPiece(new ChessPosition(7, 3), new Queen(TeamColor.BLACK));
        addPiece(new ChessPosition(7, 4), new King(TeamColor.BLACK));
        addPiece(new ChessPosition(7, 5), new Bishop(TeamColor.BLACK));
        addPiece(new ChessPosition(7, 6), new Knight(TeamColor.BLACK));
        addPiece(new ChessPosition(7, 7), new Rook(TeamColor.BLACK));
        for (int i = 0; i < 8; i++) {
            addPiece(new ChessPosition(6, i), new Pawn(TeamColor.BLACK));
        }

    }



}
