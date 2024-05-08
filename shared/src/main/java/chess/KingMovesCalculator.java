package chess;

import java.util.Collection;
import java.util.ArrayList;

public class KingMovesCalculator {

    Collection<ChessMove> ValidMovesCalculator;

    public KingMovesCalculator(ChessBoard board, ChessPosition position) {

        ValidMovesCalculator = new ArrayList<>();
        ValidMovesCalculator testClass = new ValidMovesCalculator();

        //^
        ChessPosition testPosition = new ChessPosition(position.getRow() + 1, position.getColumn());
        if(testPosition.getRow() <= 8)
            testClass.testMove(ValidMovesCalculator, board, position, testPosition);


        //->
        testPosition = new ChessPosition(position.getRow(), position.getColumn() + 1);
        if(testPosition.getColumn() <=8)
            testClass.testMove(ValidMovesCalculator, board, position, testPosition);

        //<
        testPosition = new ChessPosition(position.getRow(), position.getColumn() - 1);
        if(testPosition.getColumn() >= 1)
            testClass.testMove(ValidMovesCalculator, board, position, testPosition);

        //D ->
        testPosition = new ChessPosition(position.getRow() - 1, position.getColumn() + 1);
        if((testPosition.getRow() >= 1) && (testPosition.getColumn() <= 8))
            testClass.testMove(ValidMovesCalculator, board, position, testPosition);

        //^ ->
        testPosition = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
        if((testPosition.getRow() <= 8) && (testPosition.getColumn() >= 1))
            testClass.testMove(ValidMovesCalculator, board, position, testPosition);

        //^ <-
        testPosition = new ChessPosition(position.getRow() + 1, position.getColumn() - 1);
        if((testPosition.getRow() <= 8) && (testPosition.getColumn() >= 1))
            testClass.testMove(ValidMovesCalculator, board, position, testPosition);

        //D
        testPosition = new ChessPosition(position.getRow() - 1, position.getColumn());
        if(testPosition.getRow() >= 1)
            testClass.testMove(ValidMovesCalculator, board, position, testPosition);

        //D <-
        testPosition = new ChessPosition(position.getRow() - 1, position.getColumn() - 1);
        if((testPosition.getRow() >= 1) && (testPosition.getColumn() >= 1))
            testClass.testMove(ValidMovesCalculator, board, position, testPosition);




    }

    public Collection<ChessMove> getKingMoves () {
        return ValidMovesCalculator;
    }
}