package chess;

import java.util.Collection;
import java.util.ArrayList;

/**
 * Calculates valid moves for a Pawn on a chessboard.
 */
public class CalculatePawnMoves {

    Collection<ChessMove> validMovesCalculator;

    public CalculatePawnMoves(ChessBoard board, ChessPosition startPosition) {

        validMovesCalculator = new ArrayList<>();
        ValidMovesCalculator validMoves = new ValidMovesCalculator();
        ChessPiece pawn = board.getPiece(startPosition);

        // Validate that there is a pawn at the start position
        if (pawn == null || pawn.getPieceType() != ChessPiece.PieceType.PAWN) {
            return; // No pawn to move
        }

        ChessGame.TeamColor pieceColor = pawn.getTeamColor();
        int direction = (pieceColor == ChessGame.TeamColor.WHITE) ? 1 : -1;
        int startingRow = (pieceColor == ChessGame.TeamColor.WHITE) ? 2 : 7;
        int promotionRow = (pieceColor == ChessGame.TeamColor.WHITE) ? 8 : 1;

        // Forward one square
        ChessPosition forwardOne = new ChessPosition(startPosition.getRow() + direction, startPosition.getColumn());
        if (validMoves.isInBoard(forwardOne) && pawnTest(board, startPosition, forwardOne)) {

            // Forward two squares from starting position
            if (startPosition.getRow() == startingRow) {
                ChessPosition forwardTwo = new ChessPosition(startPosition.getRow() + 2 * direction, startPosition.getColumn());
                pawnTest(board, startPosition, forwardTwo);
            }
        }

        // Captures: Diagonal left and right
        int[] captureOffsets = { -1, 1 };
        for (int dc : captureOffsets) {
            ChessPosition capturePos = new ChessPosition(startPosition.getRow() + direction, startPosition.getColumn() + dc);
            if (validMoves.isInBoard(capturePos)) {
                pawnCaptureTest(board, startPosition, capturePos);
            }
        }
    }

    /**
     * Retrieves the collection of valid Pawn moves.
     *
     * @return A collection of valid ChessMove objects for the Pawn.
     */
    public Collection<ChessMove> getPawnMoves() {
        return validMovesCalculator;
    }

    private boolean pawnTest(ChessBoard board, ChessPosition startPosition, ChessPosition endPosition) {
        if (board.getPiece(endPosition) == null) {
            addNewMove(startPosition, endPosition);
            return true;
        }
        return false;
    }


    private void pawnCaptureTest(ChessBoard board, ChessPosition startPosition, ChessPosition endPosition) {
        ChessPiece targetPiece = board.getPiece(endPosition);
        if (targetPiece != null && targetPiece.getTeamColor() != board.getPiece(startPosition).getTeamColor()) {
            addNewMove(startPosition, endPosition);
        }
    }

    private void addNewMove(ChessPosition currentPosition, ChessPosition newPosition) {
        if (newPosition.getRow() == 8 || newPosition.getRow() == 1) {
            // Handle promotion by adding all possible promotion pieces
            ChessPiece.PieceType[] promotionPieces = {
                    ChessPiece.PieceType.QUEEN,
                    ChessPiece.PieceType.ROOK,
                    ChessPiece.PieceType.BISHOP,
                    ChessPiece.PieceType.KNIGHT
            };
            for (ChessPiece.PieceType promotion : promotionPieces) {
                validMovesCalculator.add(new ChessMove(currentPosition, newPosition, promotion));
            }
        } else {
            // Regular move without promotion
            validMovesCalculator.add(new ChessMove(currentPosition, newPosition, null));
        }
    }
}