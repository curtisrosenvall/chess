package chess;

import java.util.Collection;
import java.util.ArrayList;

public class CalculatePawnMoves extends MovesCalculator {

    private Collection<ChessMove> validMoves;

    public CalculatePawnMoves(ChessBoard board, ChessPosition startPosition) {
        validMoves = new ArrayList<>();

        ChessPiece pawn = board.getPiece(startPosition);
        if (pawn == null || pawn.getPieceType() != ChessPiece.PieceType.PAWN) {
            return; // No pawn at the start position
        }

        ChessGame.TeamColor teamColor = pawn.getTeamColor();
        int direction = (teamColor == ChessGame.TeamColor.WHITE) ? 1 : -1;
        int startRow = startPosition.getRow();
        int startColumn = startPosition.getColumn();

        // One step forward
        ChessPosition oneStepForward = new ChessPosition(startRow + direction, startColumn);
        if (isInBoard(oneStepForward) && board.getPiece(oneStepForward) == null) {
            addMove(startPosition, oneStepForward);

            // Two steps forward from starting position
            int pawnStartRow = (teamColor == ChessGame.TeamColor.WHITE) ? 2 : 7;
            if (startRow == pawnStartRow) {
                ChessPosition twoStepsForward = new ChessPosition(startRow + 2 * direction, startColumn);
                if (isInBoard(twoStepsForward) && board.getPiece(twoStepsForward) == null) {
                    addMove(startPosition, twoStepsForward);
                }
            }
        }

        // Diagonal captures
        int[] diagonalOffsets = {-1, 1};
        for (int offset : diagonalOffsets) {
            ChessPosition capturePosition = new ChessPosition(startRow + direction, startColumn + offset);
            if (isInBoard(capturePosition)) {
                ChessPiece targetPiece = board.getPiece(capturePosition);
                if (targetPiece != null && targetPiece.getTeamColor() != teamColor) {
                    addMove(startPosition, capturePosition);
                }
            }
        }
    }

    public Collection<ChessMove> getPawnMoves() {
        return validMoves;
    }

    private void addMove(ChessPosition from, ChessPosition to) {
        int promotionRow = (from.getRow() == 7 && to.getRow() == 8) || (from.getRow() == 2 && to.getRow() == 1) ? to.getRow() : -1;
        if (promotionRow == 1 || promotionRow == 8) {
            // Add all promotion options
            ChessPiece.PieceType[] promotions = {
                    ChessPiece.PieceType.QUEEN,
                    ChessPiece.PieceType.ROOK,
                    ChessPiece.PieceType.BISHOP,
                    ChessPiece.PieceType.KNIGHT
            };
            for (ChessPiece.PieceType promotion : promotions) {
                validMoves.add(new ChessMove(from, to, promotion));
            }
        } else {
            // Regular move without promotion
            validMoves.add(new ChessMove(from, to, null));
        }
    }

    private boolean isInBoard(ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    @Override
    public Collection<ChessMove> getValidMoves() {
        return validMoves;
    }
}