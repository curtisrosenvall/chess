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

//
//
//          The AllPositions class is responsible for calculating and storing all possible moves for each team in a chess game,
//          as well as tracking the positions of the white and black kings. This class analyzes the entire chessboard to:
//          Identify the position of each king and store these positions.
//          Collect all valid moves for each chess piece on the board, segregated by team (white or black).
//          Provide methods to access and modify these positions and move lists, facilitating game state management and decision-making in chess gameplay.
//          The class ensures that every piece's potential moves are evaluated and stored, allowing for efficient game analysis and strategy development.
//



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



    public void setBlackKingPos(ChessPosition blackKingPos) {
        this.blackKingPos = blackKingPos;
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

    public void setBlackTeamMoves(Collection<ChessMove> blackTeamMoves) {
        this.blackTeamMoves = blackTeamMoves;
    }

    public void setWhiteKingPos(ChessPosition whiteKingPos) {
        this.whiteKingPos = whiteKingPos;
    }

    public ChessPosition getBlackKingPos() {
        return blackKingPos;
    }



    public void setWhiteTeamMoves(Collection<ChessMove> whiteTeamMoves) {
        this.whiteTeamMoves = whiteTeamMoves;
    }


}