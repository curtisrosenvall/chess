package chess;

import java.util.ArrayList;

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
}
