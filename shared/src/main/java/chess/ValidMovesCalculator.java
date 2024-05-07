package chess;

import java.util.Collection;
import java.util.ArrayList;

public class ValidMovesCalculator {

    private Collection<ChessMove> validMoves;

    public ValidMovesCalculator() {

    }
    public void addNewMove(Collection<ChessMove> validMoves, ChessPosition startPosistion, ChessPosition endPosistion) {
        ChessMove newMove = new ChessMove(startPosistion, endPosistion, null);
        validMoves.add(newMove);
    }
}
