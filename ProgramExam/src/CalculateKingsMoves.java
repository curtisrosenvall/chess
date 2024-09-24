import java.util.ArrayList;
import java.util.Collection;

public class CalculateKingsMoves {

    Collection<ChessMove> validMovesCalculator;

    public CalculateKingsMoves(ChessBoard board, chessPosition startingPosition) {

        validMovesCalculator = new ArrayList<>();
        ValidMovesCalculator validMove = new ValidMovesCalculator();

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] direction : directions) {
            ChessPosition endPosition = new ChessPosition(
                    startingPosition.getRow() + direction[0],
                    startingPosition.getColumn() + direction[1]
            );

            if(validMove.isInBoard(endPosition)) {
                validMove.movePiece(validMovesCalculator,board,startingPosition,endPosition);
            }
        }
    }

    public Collection<ChessMove> getKingMoves() {
        return validMovesCalculator;
    }
}
