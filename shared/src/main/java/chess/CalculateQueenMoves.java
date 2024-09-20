package chess;

import java.util.Collection;
import java.util.ArrayList;

public class CalculateQueenMoves {

    Collection<ChessMove> validMovesCalculator;

    public CalculateQueenMoves(ChessBoard board, ChessPosition startPosition) {

        validMovesCalculator = new ArrayList<>();
        ValidMovesCalculator validMove = new ValidMovesCalculator();

        //Up
        boolean validPosition = true;
        int i = 1;
        while(validPosition) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() + i, startPosition.getColumn());
            validPosition = validMove.checkBoardLimits(validMovesCalculator, board, startPosition, endPosition);
            i++;
        }

        //Up and Right
        validPosition = true;
        i = 1;
        while(validPosition) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() + i, startPosition.getColumn() + i);
            validPosition = validMove.checkBoardLimits(validMovesCalculator, board, startPosition, endPosition);
            i++;
        }

        //Right
        validPosition = true;
        i = 1;
        while(validPosition) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow(), startPosition.getColumn() + i);
            validPosition = validMove.checkBoardLimits(validMovesCalculator, board, startPosition, endPosition);
            i++;
        }

        //Down and Right
        validPosition = true;
        i = 1;
        while(validPosition) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() - i, startPosition.getColumn() + i);
            validPosition = validMove.checkBoardLimits(validMovesCalculator, board, startPosition, endPosition);
            i++;
        }

        //Down
        validPosition = true;
        i = 1;
        while(validPosition) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() - i, startPosition.getColumn());
            validPosition = validMove.checkBoardLimits(validMovesCalculator, board, startPosition, endPosition);
            i++;
        }

        //Down and Left
        validPosition = true;
        i = 1;
        while(validPosition) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() - i, startPosition.getColumn() - i);
            validPosition = validMove.checkBoardLimits(validMovesCalculator, board, startPosition, endPosition);
            i++;
        }

        //Left
        validPosition = true;
        i = 1;
        while(validPosition) {
            ChessPosition testPos = new ChessPosition(startPosition.getRow(), startPosition.getColumn() - i);
            validPosition = validMove.checkBoardLimits(validMovesCalculator, board, startPosition, testPos);
            i++;
        }

        //Up and Right
        validPosition = true;
        i = 1;
        while(validPosition) {
            ChessPosition testPos = new ChessPosition(startPosition.getRow() + i, startPosition.getColumn() - i);
            validPosition = validMove.checkBoardLimits(validMovesCalculator, board, startPosition, testPos);
            i++;
        }
    }

    public Collection<ChessMove> getQueenMoves() {
        return validMovesCalculator;
    }
}