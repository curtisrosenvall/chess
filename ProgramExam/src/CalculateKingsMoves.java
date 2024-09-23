import java.util.ArrayList;
import java.util.Collection;

public class CalculateKingsMoves {

    Collection<ChessMoves> validMovesCalculator;

    public CalculateKingsMoves(ChessBoard board, ChessPosition startPosition) {
        validMovesCalculator = new ArrayList<>();
        ValidMovesCalculator validMoves = new ValidMovesCalculator();


        int[][] moveOffsets {
            {2,1},{1,2}, {-1,2}, {-2,1}, {-2,-1}, {-1,-2}, {1,-2}, {2,-1}
        }

        for(int[] moveOffset : moveOffsets) {
            ChessPosition endPosition = new ChessPosition(
                    startPosition.getRow() + offset[0]
                    startPosition.getColumn() + offset[1]
            );

            if (validMove.isIngBoard(endPosition)) {

                valdiMove.movePiece(validMovesCalculator, board,startPosition, endPosition)
            }
        }
    }
    public Collection<ChessMove> getKnightMoves() {
        return validMovesCalculator
    }
}
