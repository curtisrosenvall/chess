package chess;


import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor colorTurn;
    private ChessBoard board;
    private boolean gameOver;

    public ChessGame() {
        colorTurn = TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
        gameOver = false;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() { return colorTurn; }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) { colorTurn = team; }

    private void changeTeamTurn() {
        colorTurn = (colorTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        if (piece.getTeamColor() != colorTurn) {
            return null;
        }

        Collection<ChessMove> possibleMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new HashSet<>();

        for (ChessMove move : possibleMoves) {
            ChessGame testGame = this.cloneGame();
            try {
                testGame.makeMove(move);
                if (!testGame.isInCheck(colorTurn)) {
                    validMoves.add(move);
                }
            } catch (InvalidMoveException e) {
                // Skip invalid moves
            }
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(isGameOver())
            throw new InvalidMoveException("Game is over, no more moves can be made");

        if(move == null)
            throw new InvalidMoveException("Invalid move");

        //First check to see if the move is valid
        ChessPosition checkPos = move.getEndPosition();
        if((checkPos.getRow() < 1) || (checkPos.getRow() > 8) || (checkPos.getColumn() < 1) || (checkPos.getColumn() > 8)) {
            throw new InvalidMoveException("Invalid move (move is off the board)");
        }
        //Get a copy of the piece at the starting position
        ChessBoard moveBoard = getCopy();
        ChessPiece piece = moveBoard.getPiece(move.getStartPosition());

        if(moveBoard.getPiece(move.getStartPosition()) == null)
            throw new InvalidMoveException("There is no piece at the starting position");

        if(!checkCase && (moveBoard.getPiece(move.getStartPosition()).getTeamColor() != colorTurn))
            throw new InvalidMoveException("It is not your turn");


        if(piece.pieceMoves(moveBoard, move.getStartPosition()).contains(move)) {
            //Set the starting position to null
            moveBoard.addPiece(move.getStartPosition(), null);

            //If the promotion is null, then set the new position to the copy of the piece
            if(move.getPromotionPiece() == null) {
                moveBoard.addPiece(move.getEndPosition(), piece);
            }
            //If the promotion isn't null, then set the new position to the promotion piece
            else {
                moveBoard.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
            }
        } else {
            throw new InvalidMoveException("Not a valid move for this piece");
        }

        setBoard(moveBoard);
        if(!isInCheck(moveBoard.getPiece(move.getEndPosition()).getTeamColor())) {
            setBoard(moveBoard);
            changeTeamTurn();
            if(isInCheckmate(getTeamTurn())) {
                gameOver = true;
            }
        } else
            throw new InvalidMoveException("Invalid move, this move will put you in check");
        checkCase = false;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        checkCase = true;
        ChessBoard moveBoard = getCopy();
        AllPositions allPieces = new AllPositions(moveBoard);

        if(teamColor == TeamColor.WHITE) {
            ChessPosition whiteKingPos = allPieces.getWhiteKingPos();
            Collection<ChessMove> blackMoves = allPieces.getBlackTeamMoves();
            for (ChessMove move : blackMoves) {
                if (move.getEndPosition().equals(whiteKingPos)) {
                    return true;
                }
            }
        } else {
            ChessPosition blackKingPos = allPieces.getBlackKingPos();
            Collection<ChessMove> whiteMoves = allPieces.getWhiteTeamMoves();
            for (ChessMove move : whiteMoves) {
                if (move.getEndPosition().equals(blackKingPos)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor))
            return false;

        return anyValidMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        //Check to see which team color to check
        if(isInCheck(teamColor))
            return false;

        return anyValidMoves(teamColor);
    }

    public boolean anyValidMoves(TeamColor teamColor) {
        ChessBoard moveBoard = getCopy();
        AllPositions allPieces = new AllPositions(moveBoard);

        if(teamColor == TeamColor.WHITE) {
            Collection<ChessMove> whiteMoves = allPieces.getWhiteTeamMoves();
            for (ChessMove move : whiteMoves) {
                ChessGame testMove = new ChessGame();
                ChessBoard testBoard = this.getCopy();
                testMove.isCheckCase();
                testMove.setBoard(testBoard);
                testMove.setTeamTurn(colorTurn);
                try {
                    testMove.makeMove(move);
                } catch(InvalidMoveException e) {
                    continue;
                }
                if(!testMove.isInCheck(TeamColor.WHITE)) {
                    return false;
                }
            }
        } else {
            Collection<ChessMove> blackMoves = allPieces.getBlackTeamMoves();
            for (ChessMove move : blackMoves) {
                ChessGame testMove = new ChessGame();
                ChessBoard testBoard = this.getCopy();
                testMove.isCheckCase();
                testMove.setBoard(testBoard);
                testMove.setTeamTurn(colorTurn);
                try {
                    testMove.makeMove(move);
                } catch(InvalidMoveException e) {
                    continue;
                }
                if(!testMove.isInCheck(TeamColor.BLACK)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        newGame = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return newGame;
    }

    public ChessBoard getCopy() {
        try {
            return newGame.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public void isCheckCase() {
        checkCase = true;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "colorTurn=" + colorTurn +
                ", newGame=" + newGame +
                '}';
    }
}