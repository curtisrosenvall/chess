import java.util.ArrayList;
import java.util.Collection;

public class CalculatePawnMoves {

    Collection<ChessMove> validMovesCalculator;

    public CalculatePawnMoves(ChessBoard board, ChessPosiion startPostion) {

        validMovesCalculator = new ArrayList<>();
        ValidMovesCalculator validMoves = new ValidMovesCalculator;
        ChessPiece pawn = board.getPiece(startPostion);

        int direction = (pieceColor == ChessGame.TeamColor.White) ? 1 : -1;
        int startingRow = (pieceColor == ChessGame.TeamColor.White) ? 2 : 7;

        ChessPosition forwardOne = new ChessPosition(startPosition.getRow() + direction, startPostion.getColumn());
        if(validMoves.isInBoard(forwardOne) && pawnTest(board,startPostion,forwardOne)) {
            if(startPostion.getRow() == startingRow) {
                ChessPosition forwardTwo = new ChessPosition(startPosition.getRow() + 2 * direction, startPostion.getColumn());
                pawnTest(board,startPostion,forwardTwo);
            }
        }

        int[] captureOffsets = {{-1,1}};
        for(int dc : captureOffsets) {
            ChessPosition capturePosition = new ChessPosition(
                    startPostion.getRow() + direction,
                    startPostion.getColumn() +dc
            );
            if(validMoves.isInBounds(capturePosition)) {
                pawnCaptureTest(board,startPostion,capturePosition);
            }
        }

    }

    public Collection<ChessMove> getPawnMoves() {
        return validMovesCalculator;
    }

    private boolean pawnTest(ChessBoard board, ChessPosition startPosition, ChessPosition endPosition) {
        if(board.getPiece(endPosition) == null) {
            addNewMove(startPosition,endPosition);
            return true;
        }
        return false;
    }

    private void pawnCaptureTest(ChessBoard board, ChessPosition startPosition, ChessPosition endPosition) {
        ChessPiece captureTarget = board.getPiece(endPosition);
        if(captureTarget != null && captureTarget.getTeamColor() != board.getPiece(startPosition).getTeamColor()) {
            addNewMove(startPosition,endPosition);
        }
    }

    private void addNewMove(ChessPosition currentPosition, ChessPosition newPosition) {
        if(newPosition.getRow() == 8 || newPosition.getRow() == 1) {
            ChessPiece.PieceType[] promotionPieces = {
                    ChessPiece.PieceType.Queen,
                    ChessPiece.PieceType.ROOK,
                    ChessPiece.PieceType.BISHOP,
                    ChessPiece.PieceType.KNIGHT
            };
            for(ChessPIece.PIeceType promotion: promotionPieces) {
                validMovesCalculator.add(new ChessMove(currentPosition, newPosition, promotion));
            }
        } else {
            validMovesCalculator.add(new ChessMove(currentPosition, newPosition, null));


        }

    }




}
