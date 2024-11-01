package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AllPiecePositions {

    private ChessPosition whiteKingPos;
    private ChessPosition blackKingPos;
    private List<ChessMove> whiteTeamMoves;
    private List<ChessMove> blackTeamMoves;

    public AllPiecePositions(ChessBoard board) {
        whiteTeamMoves = new ArrayList<>();
        blackTeamMoves = new ArrayList<>();

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);

                if (piece != null) {
                    processPiece(piece, position, board);
                }
            }
        }
    }

    private void processPiece(ChessPiece piece, ChessPosition position, ChessBoard board) {
        ChessGame.TeamColor teamColor = piece.getTeamColor();
        ChessPiece.PieceType pieceType = piece.getPieceType();

        if (pieceType == ChessPiece.PieceType.KING && teamColor == ChessGame.TeamColor.WHITE) {
            whiteKingPos = position;
        } else if (pieceType == ChessPiece.PieceType.KING && teamColor == ChessGame.TeamColor.BLACK) {
            blackKingPos = position;
        }

        Collection<ChessMove> moves = piece.pieceMoves(board, position);
        if (moves != null) {
            List<ChessMove> targetMoves = (teamColor == ChessGame.TeamColor.WHITE)
                    ? whiteTeamMoves
                    : blackTeamMoves;
            targetMoves.addAll(moves);
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