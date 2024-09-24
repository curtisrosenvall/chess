import java.util.ArrayList;
import java.util.Collection;

public class ValidMovesCalculator {

    public ValidMovesCalculator() {
        validMovesCalculator = new ArrayList<>();
    }

    public boolean movePiece (Collection<ChessMove> validMoves, ChessBoard board, ChessPosition startPosition, ChessPosition endPosition) {
        if(board.getPiece(endPosition) == null) {
            addNewMove(validMoves,startPosition,endPosition);
            return true;
        } else if (board.getPiece(endPosition).getTeamColor() == board.getPiece(startPosition).getTeamColor()) {
            return false;
        } else {
            addNewMove(validMoves,startPosition,endPosition);
            return false;
        }
    }

    public void addNewMove(Collection<ChessMove> validMoves, ChessPosition startPosition, ChessPosition endPosition) {
        ChessMove newMove = new ChessMove(startPosition,endPosition,null);
        validMoves.add(newMove);
    }


    public boolean isInBoard(ChessPosition endPosition) {
        return(endPosition.getRow() <= 8) && (endPosition.getRow() >= 1) && (endPosition.getColumn() >= 1) && (endPosition.getColumn() <= 8);
    }


}
