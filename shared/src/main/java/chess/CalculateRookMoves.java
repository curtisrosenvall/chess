package chess;

import java.util.Collection;
import java.util.ArrayList;

public class CalculateRookMoves {

    Collection<ChessMove> validMovesCalculator;

    public CalculateRookMoves(ChessBoard board, ChessPosition startPosition) {

        validMovesCalculator = new ArrayList<>();
        ValidMovesCalculator validMove = new ValidMovesCalculator();
        ChessPiece rook = board.getPiece(startPosition);


        if (rook == null || rook.getPieceType() != ChessPiece.PieceType.ROOK) {
            return; // No rook to move
        }

        int[][] directions = {
                {1, 0},   // Up
                {-1, 0},  // Down
                {0, 1},   // Right
                {0, -1}   // Left
        };

        for (int[] direction : directions) {
            int rowOffset = direction[0];
            int colOffset = direction[1];
            int step = 1;

            while (true) {
                ChessPosition endPosition = new ChessPosition(
                        startPosition.getRow() + step * rowOffset,
                        startPosition.getColumn() + step * colOffset
                );
                if (!validMove.isInBoard(endPosition)) {
                    break; // Move is off the board
                }

                boolean canContinue = validMove.movePiece(validMovesCalculator, board, startPosition, endPosition);

                if (!canContinue) {
                    break;
                }

                step++;
            }
        }
    }

    public Collection<ChessMove> getRookMoves() {
        return validMovesCalculator;
    }
}