package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.IntStream;

public class AllPiecePositions {

    private ChessPosition whiteKingPos;
    private ChessPosition blackKingPos;
    private final Collection<ChessMove> whiteTeamMoves;
    private final Collection<ChessMove> blackTeamMoves;

    public AllPiecePositions(ChessBoard board) {
        whiteTeamMoves = new ArrayList<>();
        blackTeamMoves = new ArrayList<>();

        IntStream.rangeClosed(1, 8).forEach(i -> {
            IntStream.rangeClosed(1, 8).forEach(j -> {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);

                if (piece != null) {
                    ChessGame.TeamColor teamColor = piece.getTeamColor();
                    ChessPiece.PieceType pieceType = piece.getPieceType();

                    if (pieceType == ChessPiece.PieceType.KING) {
                        if (teamColor == ChessGame.TeamColor.WHITE) {
                            whiteKingPos = position;
                        } else {
                            blackKingPos = position;
                        }
                    }

                    Collection<ChessMove> moves = piece.pieceMoves(board, position);
                    if (moves != null) {
                        if (teamColor == ChessGame.TeamColor.WHITE) {
                            whiteTeamMoves.addAll(moves);
                        } else {
                            blackTeamMoves.addAll(moves);
                        }
                    }
                }
            });
        });
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