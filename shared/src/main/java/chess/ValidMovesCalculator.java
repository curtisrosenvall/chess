package chess;

import java.util.Collection;

public class ValidMovesCalculator {

    private Collection<ChessMove>  ValidMovesCalculator;

    public ValidMovesCalculator() {

    }

    /*public Collection<ChessMove> getValidMoves() {
        return validMoves;
    }*/

    public boolean testMove(Collection<ChessMove> validMovesCalculator, ChessBoard board, ChessPosition startPos, ChessPosition testPosition) {
        if(board.getPiece(testPosition) == null) {
            addNewMove(validMovesCalculator, startPos, testPosition);
            return true;
        } else if (board.getPiece(testPosition).getTeamColor() == board.getPiece(startPos).getTeamColor()) {
            return false;
        } else {
            addNewMove(validMovesCalculator, startPos, testPosition);
            return false;
        }
    }

    public void addNewMove(Collection<ChessMove> validMoves, ChessPosition curPos, ChessPosition newPos) {
        ChessMove newMove = new ChessMove(curPos, newPos, null);
        validMoves.add(newMove);
    }
}