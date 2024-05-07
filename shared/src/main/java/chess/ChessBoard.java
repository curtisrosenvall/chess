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

    /**
     * Moves a chess piece from one position to another.
     * @param from The starting position of the piece.
     * @param to The destination position of the piece.
     * @return true if the move was successful, false otherwise.
     */
    public boolean movePiece(ChessPosition from, ChessPosition to) {
        if (!isPositionValid(from) || !isPositionValid(to)) {
            return false;  // Check if both positions are valid.
        }

        ChessPiece movingPiece = getPiece(from);
        if (movingPiece == null) {
            return false;  // No piece at the source position.
        }

        // Optionally add more checks here for move legality according to chess rules.

        // Move the piece
        squares[to.getRow()][to.getColumn()] = movingPiece;
        squares[from.getRow()][from.getColumn()] = null;
        return true;
    }



    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        // Check if the position is valid
        if (!isPositionValid(position)) {
            return; // or throw an IllegalArgumentException indicating invalid position
        }

        // Place the piece at the specified position on the chessboard
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
        // Check if the position is valid
        if (!isPositionValid(position)) {
            return null;
        }
        // Get the piece at the specified position
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
