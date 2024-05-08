package chess;

import java.util.Collection;
import java.util.ArrayList;

public class KnightMovesCalculator {

    Collection<ChessMove> ValidMovesCalculator;

    public KnightMovesCalculator(ChessBoard board, ChessPosition position) {

        ValidMovesCalculator = new ArrayList<>();
        ValidMovesCalculator testClass = new ValidMovesCalculator();

        //2 -> 1
        ChessPosition testPosition = new ChessPosition(position.getRow() + 2, position.getColumn() + 1);
        if((testPosition.getRow() <= 8) && (testPosition.getColumn() <= 8)) {
            testClass.testMove(ValidMovesCalculator, board, position, testPosition);
        }

        //U 1 <- 2
        testPosition = new ChessPosition(position.getRow() + 1, position.getColumn() - 2);
        if((testPosition.getRow() <= 8) && (testPosition.getColumn() >= 1)) {
            testClass.testMove(ValidMovesCalculator, board, position, testPosition);
        }

        //D 1 -> 2
        testPosition = new ChessPosition(position.getRow() - 1, position.getColumn() + 2);
        if((testPosition.getRow() >= 1) && (testPosition.getColumn() <= 8)) {
            testClass.testMove(ValidMovesCalculator, board, position, testPosition);
        }

        //2 -> 2
        testPosition = new ChessPosition(position.getRow() + 1, position.getColumn() + 2);
        if((testPosition.getRow() <= 8) && (testPosition.getColumn() <= 8)) {
            testClass.testMove(ValidMovesCalculator, board, position, testPosition);
        }


        //D 2 -> 1
        testPosition = new ChessPosition(position.getRow() - 2, position.getColumn() + 1);
        if((testPosition.getRow() >= 1) && (testPosition.getColumn() <= 8)) {
            testClass.testMove(ValidMovesCalculator, board, position, testPosition);
        }

        //U 2 <- 1
        testPosition = new ChessPosition(position.getRow() + 2, position.getColumn() - 1);
        if((testPosition.getRow() <= 8) && (testPosition.getColumn() >= 1)) {
            testClass.testMove(ValidMovesCalculator, board, position, testPosition);
        }

        //D 2 <- 1
        testPosition = new ChessPosition(position.getRow() - 2, position.getColumn() - 1);
        if((testPosition.getRow() >= 1) && (testPosition.getColumn() >=1)) {
            testClass.testMove(ValidMovesCalculator, board, position, testPosition);
        }

        //D 1 <- 2
        testPosition = new ChessPosition(position.getRow() - 1, position.getColumn() - 2);
        if((testPosition.getRow() >= 1) && (testPosition.getColumn() >= 1)) {
            testClass.testMove(ValidMovesCalculator, board, position, testPosition);
        }




    }

    public Collection<ChessMove> getKnightMoves() {
        return ValidMovesCalculator;
    }
}