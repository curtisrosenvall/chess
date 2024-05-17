package chess;

import java.util.ArrayList;
import java.util.Collection;

public class ValidMovesCalculator {

    private Collection<ChessMove> validMovesCalculator;

    public ValidMovesCalculator() {
        validMovesCalculator = new ArrayList<>();
    }

    public boolean LoopTest(Collection<ChessMove> validMoves, ChessBoard board, ChessPosition startPosition, ChessPosition endPosition) {
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