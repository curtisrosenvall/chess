package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor colorTurn;
    private boolean gameOver;
    private ChessBoard board;

    public ChessGame() {
        colorTurn = TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
        gameOver = false;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return colorTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        colorTurn = team;
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
        if(piece == null) {
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
        if (gameOver) {
            throw new InvalidMoveException("Game is over, no more moves can be made");
        }
        if (move == null) {
            throw new InvalidMoveException("Invalid move");
        }

        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece == null || piece.getTeamColor() != colorTurn) {
            throw new InvalidMoveException("Invalid piece selection");
        }

        Collection<ChessMove> possibleMoves = piece.pieceMoves(board, move.getStartPosition());
        if (!possibleMoves.contains(move)) {
            throw new InvalidMoveException("Not a valid move for this piece");
        }

        // Simulate the move
        ChessBoard tempBoard = board.clone();
        tempBoard.movePiece(move);

        if (isInCheck(tempBoard, colorTurn)) {
            throw new InvalidMoveException("Move would put or leave king in check");
        }

        // Make the move
        board.movePiece(move);

        // Check for checkmate or stalemate
        switchTurn();
        if (isInCheckmate(colorTurn)) {
            gameOver = true;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {

    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        throw new RuntimeException("Not implemented");
    }
}
