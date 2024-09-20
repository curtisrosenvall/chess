package chess;

import java.util.Collection;

public class ValidMovesCalculator {

    /**
     * Checks if the end position is within the board and attempts to move the piece.
     *
     * @param validMoves    The collection to add valid moves.
     * @param board         The current chess board.
     * @param startPosition The starting position of the piece.
     * @param endPosition   The target position to move to.
     * @return true if the move is on the board and the path is not blocked by a friendly piece.
     */
    public static boolean checkBoardLimitsAndMove(Collection<ChessMove> validMoves, ChessBoard board, ChessPosition startPosition, ChessPosition endPosition) {
        if (isInBoard(endPosition)) {
            return movePiece(validMoves, board, startPosition, endPosition);
        }
        return false;
    }

    /**
     * Checks if a position is within the bounds of the chess board.
     *
     * @param position The position to check.
     * @return true if the position is valid, false otherwise.
     */
    public static boolean isInBoard(ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    /**
     * Attempts to move a piece from start to end position. If the end position is empty or occupied by an opponent's piece,
     * adds the move to validMoves.
     *
     * @param validMoves    The collection to add valid moves.
     * @param board         The current chess board.
     * @param startPosition The starting position of the piece.
     * @param endPosition   The target position to move to.
     * @return true if the end position is empty (move can continue in this direction), false otherwise.
     */
    public static boolean movePiece(Collection<ChessMove> validMoves, ChessBoard board, ChessPosition startPosition, ChessPosition endPosition) {
        ChessPiece startPiece = board.getPiece(startPosition);
        ChessPiece endPiece = board.getPiece(endPosition);

        if (endPiece == null) {
            addNewMove(validMoves, startPosition, endPosition);
            return true; // Move can continue in this direction
        } else if (endPiece.getTeamColor() != startPiece.getTeamColor()) {
            addNewMove(validMoves, startPosition, endPosition);
            return false; // Capture and stop in this direction
        } else {
            return false; // Blocked by friendly piece, stop in this direction
        }
    }

    /**
     * Adds a new move to the collection of valid moves.
     *
     * @param validMoves    The collection to add the move to.
     * @param startPosition The starting position of the move.
     * @param endPosition   The target position of the move.
     */
    public static void addNewMove(Collection<ChessMove> validMoves, ChessPosition startPosition, ChessPosition endPosition) {
        validMoves.add(new ChessMove(startPosition, endPosition, null));
    }
}