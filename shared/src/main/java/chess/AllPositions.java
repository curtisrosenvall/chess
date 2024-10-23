package chess;

import java.util.ArrayList;
import java.util.Collection;

public class AllPositions {

    private ChessPosition whiteKingPos;
    private ChessPosition blackKingPos;
    private Collection<ChessMove> whiteTeamMoves;
    private Collection<ChessMove> blackTeamMoves;

    public AllPositions(ChessBoard board) {
        whiteTeamMoves = new ArrayList<>();
        blackTeamMoves = new ArrayList<>();



        for(int i = 1; i < 9; i++) {
            for(int j = 1; j < 9; j++) {
                if((board.getPiece(new ChessPosition(i,j)) != null)) {
                    if(board.getPiece(new ChessPosition(i,j)).getPieceType() == ChessPiece.PieceType.KING) {
                        if(board.getPiece(new ChessPosition(i,j)).getTeamColor() == ChessGame.TeamColor.WHITE) {
                            whiteKingPos = new ChessPosition(i, j);
                            if(board.getPiece(whiteKingPos).pieceMoves(board, whiteKingPos) != null)
                                whiteTeamMoves.addAll(board.getPiece(whiteKingPos).pieceMoves(board, whiteKingPos));
                        } else {
                            blackKingPos = new ChessPosition(i, j);
                            if(board.getPiece(blackKingPos).pieceMoves(board, blackKingPos) != null)
                                blackTeamMoves.addAll(board.getPiece(blackKingPos).pieceMoves(board, blackKingPos));
                        }
                    } else if(board.getPiece(new ChessPosition(i,j)).getTeamColor() == ChessGame.TeamColor.BLACK) {
                        if(board.getPiece(new ChessPosition(i,j)).pieceMoves(board, new ChessPosition(i,j)) != null)
                            blackTeamMoves.addAll(board.getPiece(new ChessPosition(i,j)).pieceMoves(board, new ChessPosition(i,j)));
                    } else {
                        if(board.getPiece(new ChessPosition(i,j)).pieceMoves(board, new ChessPosition(i,j)) != null)
                            whiteTeamMoves.addAll(board.getPiece(new ChessPosition(i,j)).pieceMoves(board, new ChessPosition(i,j)));
                    }
                }
            }
        }
    }





    public Collection<ChessMove> getWhiteTeamMoves() {
        return whiteTeamMoves;
    }

    public ChessPosition getWhiteKingPos() {
        return whiteKingPos;
    }

    public Collection<ChessMove> getBlackTeamMoves() {
        return blackTeamMoves;
    }

    public ChessPosition getBlackKingPos() {
        return blackKingPos;
    }




}