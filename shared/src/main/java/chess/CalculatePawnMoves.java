package chess;

import java.util.Collection;
import java.util.ArrayList;

public class CalculatePawnMoves {

    Collection<ChessMove> validMovesCalculator;

    public CalculatePawnMoves(ChessBoard board, ChessPosition startPosition) {

        validMovesCalculator = new ArrayList<>();
        ValidMovesCalculator validMoves = new ValidMovesCalculator();
        ChessGame.TeamColor pieceColor = board.getPiece(startPosition).getTeamColor();
        boolean validPosition = true;

        //If White
        if(pieceColor == ChessGame.TeamColor.WHITE) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn());
            if(validMoves.isInBoard(endPosition))
                validPosition = pawnTest(board, startPosition, endPosition);
            if((startPosition.getRow() == 2) && validPosition) {
                endPosition = new ChessPosition(startPosition.getRow() + 2, startPosition.getColumn());
                pawnTest(board, startPosition, endPosition);
            }
            endPosition  = new ChessPosition( startPosition.getRow() + 1, startPosition.getColumn() - 1);
            if(validMoves.isInBoard(endPosition))
                pawnCaptureTest(board, startPosition, endPosition);
            endPosition = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 1);
            if(validMoves.isInBoard(endPosition))
                pawnCaptureTest(board, startPosition, endPosition);

        }
        //Else If Black
        else if (pieceColor == ChessGame.TeamColor.BLACK) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn());
            if(validMoves.isInBoard(endPosition))
                validPosition = pawnTest(board, startPosition, endPosition);
            if((startPosition.getRow() == 7) && validPosition) {
                endPosition = new ChessPosition(startPosition.getRow() - 2, startPosition.getColumn());
                pawnTest(board, startPosition, endPosition);
            }
            endPosition = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() - 1);
            if(validMoves.isInBoard(endPosition))
                pawnCaptureTest(board, startPosition, endPosition);
            endPosition = new ChessPosition( startPosition.getRow() - 1, startPosition.getColumn() + 1);
            if(validMoves.isInBoard(endPosition))
                pawnCaptureTest(board, startPosition, endPosition);
        } else {
            validMovesCalculator = null;
        }
    }

    public Collection<ChessMove> getPawnMoves() {
        return validMovesCalculator;
    }

    public boolean pawnTest(ChessBoard board, ChessPosition startPosition, ChessPosition endPosition) {
        if((board.getPiece(endPosition) == null)) {
            addNewMove(startPosition, endPosition);
            return true;
        } else
            return false;
    }

    public void pawnCaptureTest(ChessBoard board, ChessPosition startPosition, ChessPosition endPosition) {
        if((board.getPiece(endPosition) != null) && (board.getPiece(endPosition).getTeamColor() != board.getPiece(startPosition).getTeamColor()))
            addNewMove(startPosition, endPosition);
    }

    public void addNewMove(ChessPosition currentPosition, ChessPosition newPosition) {
        if((newPosition.getRow() == 8) || (newPosition.getRow() == 1)) {
            ChessMove newQueenMove = new ChessMove(currentPosition, newPosition, ChessPiece.PieceType.QUEEN);
            validMovesCalculator.add(newQueenMove);
            ChessMove newRookMove = new ChessMove(currentPosition, newPosition, ChessPiece.PieceType.ROOK);
            validMovesCalculator.add(newRookMove);
            ChessMove newBishopMove = new ChessMove(currentPosition, newPosition, ChessPiece.PieceType.BISHOP);
            validMovesCalculator.add(newBishopMove);
            ChessMove newKnightMove = new ChessMove(currentPosition, newPosition, ChessPiece.PieceType.KNIGHT);
            validMovesCalculator.add(newKnightMove);
        } else {
            ChessMove newMove = new ChessMove(currentPosition, newPosition, null);
            validMovesCalculator.add(newMove);
        }

    }
}