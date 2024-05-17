package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Calculates valid moves for chess pieces on a chessboard.
 * This class encapsulates the logic for determining valid movements
 * based on chess rules and the current state of the board.
 */

public class ValidMovesCalculator {

    private Collection<ChessMove> validMovesCalculator;

    public ValidMovesCalculator() {
        validMovesCalculator = new ArrayList<>();
    }

    /**
     * Tests if a move from a start position to an end position is within the board limits
     * and if so, tries to execute the move.
     *
     * @param validMoves Collection where the valid move will be added if successful.
     * @param board The chess board on which the move is being made.
     * @param startPosition The starting position of the move.
     * @param endPosition The end position of the move.
     * @return true if the move is within the board and successfully executed, false otherwise.
     */

    public boolean checkBoardLimits(Collection<ChessMove> validMoves, ChessBoard board, ChessPosition startPosition, ChessPosition endPosition) {
        if(isInBoard(endPosition))
            return movePiece(validMoves, board, startPosition, endPosition);
        else
            return false;
    }

    public boolean movePiece(Collection<ChessMove> validMoves, ChessBoard board, ChessPosition startPosition, ChessPosition endPosition) {
        if(board.getPiece(endPosition) == null) {
            addNewMove( validMoves, startPosition, endPosition);
            return true;
        } else if (board.getPiece(endPosition).getTeamColor() == board.getPiece(startPosition).getTeamColor()) {
            return false;
        } else {
            addNewMove(validMoves, startPosition, endPosition);
            return false;
        }
    }

    public void addNewMove(Collection<ChessMove> validMoves, ChessPosition startPosition, ChessPosition endPosition) {
        ChessMove newMove = new ChessMove(startPosition, endPosition, null);
        validMoves.add(newMove);
    }

    public boolean isInBoard(ChessPosition endPosition) {
        return (endPosition.getRow() <= 8) && (endPosition.getRow() >= 1) && (endPosition.getColumn() >= 1) && (endPosition.getColumn() <= 8);
    }
}