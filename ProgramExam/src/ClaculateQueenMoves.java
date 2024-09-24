import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class ClaculateQueenMoves {

   Collection<ChessMove> validMovesCalculator;

   public CalculateQueenMoves(ChessBoard board, ChessPosition startingPosition) {

       validMovesCalculator = new ArrayList<>();
       ValidMovesCalculator validMove = new ValidMovesCalculator();


       int[][] directions = {

       }

       for(int[] direction : directions) {
           int rowOffset = direction[0];
           int colOffset = direction[1];
           int step = 1;

           while(true) {
               ChessPostion endPostion = new ChessPositoin(
                       startingPosition.getRow() +rowOffset,
                       startingPosition.getColumn() + colOffset
               );

               if(!validMove.isInBoard(endPostion)) {
                   break;
               }

               boolean canContinue = validMove.movePiece(validMovesCalculator,board, startingPosition, endPostion);

               if(!canContinue) {
                   break;
               }
               step++;
           }


       }
   }

   public Collection<ChessMove> getQueenMoves() {
       return validMovesCalculator;
   }

}
