package chess;

import java.util.Collection;
import java.util.ArrayList;

public class PawnMovesCalculator {

    Collection<ChessMove> ValidMovesCalculator;

    public PawnMovesCalculator(ChessBoard board, ChessPosition startPosition) {

        ValidMovesCalculator = new ArrayList<>();
        ChessGame.TeamColor pieceColor = board.getPiece(startPosition).getTeamColor();
        boolean validPosition = true;


        if(pieceColor == ChessGame.TeamColor.WHITE) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn());
            validPosition = pawnTest(board, startPosition, endPosition);
            if(startPosition.getRow() == 2 && validPosition) {
                endPosition = new ChessPosition(startPosition.getRow() + 2, startPosition.getColumn());
                validPosition = pawnTest(board, startPosition, endPosition);
            }
            endPosition  = new ChessPosition( startPosition.getRow() + 1, startPosition.getColumn() - 1);
            validPosition = pawnCapture(board, startPosition, endPosition);
            endPosition = new ChessPosition(startPosition.getRow() + 2, startPosition.getColumn() + 1);
            validPosition = pawnCapture(board, startPosition, endPosition);
        }

        else if (pieceColor == ChessGame.TeamColor.BLACK) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn());
            validPosition = pawnTest(board, startPosition, endPosition);
            if((startPosition.getRow() == 7) && validPosition) {
                endPosition = new ChessPosition(startPosition.getRow() - 2, startPosition.getColumn());
                validPosition = pawnTest(board, startPosition, endPosition);
            }
            endPosition  = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() - 1);
            validPosition = pawnCapture(board, startPosition, endPosition);
            endPosition = new ChessPosition( startPosition.getRow() - 1, startPosition.getColumn() + 1);
            validPosition = pawnCapture(board, startPosition, endPosition);
        } else {
            ValidMovesCalculator = null;
        }
    }

    public Collection<ChessMove> getPawnMoves() {
        return ValidMovesCalculator;
    }

    public boolean pawnTest(ChessBoard board, ChessPosition startPosition, ChessPosition endPosition) {
        if((endPosition.getRow() <= 8) && (endPosition.getRow() >= 1) && (endPosition.getColumn() >= 1) && (endPosition.getColumn() <= 8) && (board.getPiece(endPosition) == null)) {
            addPromotionMove(startPosition, endPosition);
            return true;
        } else
            return false;
    }

    public boolean pawnCapture(ChessBoard board, ChessPosition startPos, ChessPosition endPosition) {
        if((endPosition.getRow() <= 8) && (endPosition.getRow() >= 1) && (endPosition.getColumn() >= 1) && (endPosition.getColumn() <= 8) && (board.getPiece(endPosition) != null) && (board.getPiece(endPosition).getTeamColor() != board.getPiece(startPos).getTeamColor())) {
            addPromotionMove(startPos, endPosition);
            return true;
        } else
            return false;
    }

    public void addPromotionMove(ChessPosition startPosition, ChessPosition endPosition) {
        if((endPosition.getRow() == 8 )|| (endPosition.getRow() == 1)) {
            ChessMove newQueenMove = new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN);
            ValidMovesCalculator.add(newQueenMove);
            ChessMove newRookMove = new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK);
            ValidMovesCalculator.add(newRookMove);
            ChessMove newBishopMove = new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP);
            ValidMovesCalculator.add(newBishopMove);
            ChessMove newKnightMove = new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT);
            ValidMovesCalculator.add(newKnightMove);
        } else {
            ChessMove newMove = new ChessMove(startPosition, endPosition, null);
            ValidMovesCalculator.add(newMove);
        }

    }
}