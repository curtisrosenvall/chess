package chess;

import java.util.Collection;

public class ValidMovesCalculator {

    // Checks if a given position is within the board limits
    public static boolean isInBoard(ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    // Attempts to move a piece and adds the move to validMoves if valid
    public static boolean movePiece(Collection<ChessMove> validMoves, ChessBoard board, ChessPosition startPosition, ChessPosition endPosition) {
        ChessPiece startPiece = board.getPiece(startPosition);
        ChessPiece endPiece = board.getPiece(endPosition);

        if (endPiece == null || endPiece.getTeamColor() != startPiece.getTeamColor()) {
            addNewMove(validMoves, startPosition, endPosition);
            // Return true if the end position was empty, false if it had an opponent's piece
            return endPiece == null;
        }
        // Cannot move onto a square occupied by a friendly piece
        return false;
    }

    // Adds a new move to the collection of valid moves
    public static void addNewMove(Collection<ChessMove> validMoves, ChessPosition startPosition, ChessPosition endPosition) {
        validMoves.add(new ChessMove(startPosition, endPosition, null));
    }

    // Combines isInBoard and movePiece to check and attempt a move
    public static boolean checkBoardLimitsAndMove(Collection<ChessMove> validMoves, ChessBoard board, ChessPosition startPosition, ChessPosition endPosition) {
        if (isInBoard(endPosition)) {
            return movePiece(validMoves, board, startPosition, endPosition);
        }
        return false;
    }
}