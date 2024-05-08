package chess;

import java.util.Collection;
import java.util.ArrayList;

public class PawnMovesCalculator {

    Collection<ChessMove> ValidMovesCalculator;

    public PawnMovesCalculator(ChessBoard board, ChessPosition position) {

        ValidMovesCalculator = new ArrayList<>();
        ChessGame.TeamColor pieceColor = board.getPiece(position).getTeamColor();
        boolean valid = true;

        //If White
        if(pieceColor == ChessGame.TeamColor.WHITE) {
            ChessPosition testPosition = new ChessPosition(position.getRow() + 1, position.getColumn());
            valid = pawnTest(board, position, testPosition);
            if((position.getRow() == 2) && valid) {
                testPosition = new ChessPosition(position.getRow() + 2, position.getColumn());
                valid = pawnTest(board, position, testPosition);
            }
            testPosition = new ChessPosition( position.getRow() + 1, position.getColumn() - 1);
            valid = pawnCapture(board, position, testPosition);
            testPosition = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
            valid = pawnCapture(board, position, testPosition);
        }
        //Else If Black
        else if (pieceColor == ChessGame.TeamColor.BLACK) {
            ChessPosition testPosition = new ChessPosition(position.getRow() - 1, position.getColumn());
            valid = pawnTest(board, position, testPosition);
            if((position.getRow() == 7) && valid) {
                testPosition = new ChessPosition(position.getRow() - 2, position.getColumn());
                valid = pawnTest(board, position, testPosition);
            }
            testPosition = new ChessPosition(position.getRow() - 1, position.getColumn() - 1);
            valid = pawnCapture(board, position, testPosition);
            testPosition = new ChessPosition( position.getRow() - 1, position.getColumn() + 1);
            valid = pawnCapture(board, position, testPosition);
        } else {
            ValidMovesCalculator = null;
        }
    }

    public Collection<ChessMove> getPawnMoves() {

        return ValidMovesCalculator;
    }

    public boolean pawnTest(ChessBoard board, ChessPosition startPos, ChessPosition testPos) {
        if((testPos.getRow() <= 8) && (testPos.getRow() >= 1) && (testPos.getColumn() >= 1) && (testPos.getColumn() <= 8) && (board.getPiece(testPos) == null)) {
            addPromotionMove(startPos, testPos);
            return true;
        } else
            return false;
    }

    public boolean pawnCapture(ChessBoard board, ChessPosition startPos, ChessPosition testPos) {
        if((testPos.getRow() <= 8) && (testPos.getRow() >= 1) && (testPos.getColumn() >= 1) && (testPos.getColumn() <= 8) && (board.getPiece(testPos) != null) && (board.getPiece(testPos).getTeamColor() != board.getPiece(startPos).getTeamColor())) {
            addPromotionMove(startPos, testPos);
            return true;
        } else
            return false;
    }

    public void addPromotionMove(ChessPosition curPos, ChessPosition newPos) {
        if((newPos.getRow() == 8) || (newPos.getRow() == 1)) {
            ChessMove newQueenMove = new ChessMove(curPos, newPos, ChessPiece.PieceType.QUEEN);
            ValidMovesCalculator.add(newQueenMove);
            ChessMove newRookMove = new ChessMove(curPos, newPos, ChessPiece.PieceType.ROOK);
            ValidMovesCalculator.add(newRookMove);
            ChessMove newBishopMove = new ChessMove(curPos, newPos, ChessPiece.PieceType.BISHOP);
            ValidMovesCalculator.add(newBishopMove);
            ChessMove newKnightMove = new ChessMove(curPos, newPos, ChessPiece.PieceType.KNIGHT);
            ValidMovesCalculator.add(newKnightMove);
        } else {
            ChessMove newMove = new ChessMove(curPos, newPos, null);
            ValidMovesCalculator.add(newMove);
        }

    }
}