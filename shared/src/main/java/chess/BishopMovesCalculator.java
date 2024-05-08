package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Objects;

public class BishopMovesCalculator {

    Collection<ChessMove> ValidMovesCalculator;

    public BishopMovesCalculator(ChessBoard board, ChessPosition position) {

        ValidMovesCalculator = new ArrayList<>();
        ValidMovesCalculator testMove = new ValidMovesCalculator();

        //^ ->
        boolean valid = true;
        int i = 1;
        while (valid) {
            ChessPosition nextPosition = new ChessPosition(position.getRow() + i, position.getColumn() + i);
            if ((nextPosition.getRow() > 8) || (nextPosition.getColumn() > 8)) {
                valid = false;
            } else {
                valid = testMove.movePiece(ValidMovesCalculator, board, position, nextPosition);
            }
            i++;
        }

        //^ <-
        valid = true;
        i = 1;
        while (valid) {
            ChessPosition nextPosition = new ChessPosition(position.getRow() + i, position.getColumn() - i);
            if ((nextPosition.getRow() > 8) || (nextPosition.getColumn() < 1)) {
                valid = false;
            } else {
                valid = testMove.movePiece(ValidMovesCalculator, board, position, nextPosition);
            }
            i++;
        }

        //D <-
        valid = true;
        i = 1;
        while (valid) {
            ChessPosition nextPosition = new ChessPosition(position.getRow() - i, position.getColumn() - i);
            if ((nextPosition.getRow() < 1) || (nextPosition.getColumn() < 1)) {
                valid = false;
            } else {
                valid = testMove.movePiece(ValidMovesCalculator, board, position, nextPosition);
            }
            i++;
        }

        //D ->
        valid = true;
        i = 1;
        while (valid) {
            ChessPosition nextPosition = new ChessPosition(position.getRow() - i, position.getColumn() + i);
            if ((nextPosition.getRow() < 1) || (nextPosition.getColumn() > 8)) {
                valid = false;
            } else {
                valid = testMove.movePiece(ValidMovesCalculator, board, position, nextPosition);
            }
            i++;
        }




    }

    public Collection<ChessMove> getBishopMoves() {
        return ValidMovesCalculator;
    }
}

