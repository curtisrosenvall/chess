package chess;

import java.util.Collection;

public class ValidMovesCalculator {


    public ValidMovesCalculator() {

    }

    public boolean movePiece(Collection<ChessMove> validMovesCalculator, ChessBoard board, ChessPosition startPos, ChessPosition endPosition) {
        if(board.getPiece(endPosition) == null) {
            addNewMove(validMovesCalculator, startPos, endPosition);
            return true;
        } else if (board.getPiece(endPosition).getTeamColor() == board.getPiece(startPos).getTeamColor()) {
            return false;
        } else {
            addNewMove(validMovesCalculator, startPos, endPosition);
            return false;
        }
    }

    public void addNewMove(Collection<ChessMove> ValidMovesCalculator, ChessPosition curPos, ChessPosition newPos) {
        ChessMove newMove = new ChessMove(curPos, newPos, null);
        ValidMovesCalculator.add(newMove);
    }
}