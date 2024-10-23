package chess;

import java.util.ArrayList;
import java.util.Collection;

public class AllPiecePositions {

    private ChessPosition whiteKingPos;
    private ChessPosition blackKingPos;
    private final Collection<ChessMove> whiteTeamMoves;
    private final Collection<ChessMove> blackTeamMoves;

    public AllPiecePositions(ChessBoard board) {
        whiteTeamMoves = new ArrayList<>();
        blackTeamMoves = new ArrayList<>();

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);

                if (piece != null) {
                    ChessGame.TeamColor teamColor = piece.getTeamColor();
                    ChessPiece.PieceType pieceType = piece.getPieceType();
                    Collection<ChessMove> moves = piece.pieceMoves(board, position);

                    if (pieceType == ChessPiece.PieceType.KING) {
                        // Set the king's position based on its team color
                        if (teamColor == ChessGame.TeamColor.WHITE) {
                            whiteKingPos = position;
                        } else {
                            blackKingPos = position;
                        }
                    }

                    if (moves != null) {
                        // Add moves to the appropriate team's move list
                        if (teamColor == ChessGame.TeamColor.WHITE) {
                            whiteTeamMoves.addAll(moves);
                        } else {
                            blackTeamMoves.addAll(moves);
                        }
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