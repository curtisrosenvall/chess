package chess;


import java.util.Collection;
import java.util.Iterator;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor colorTurn;
    private boolean checkCase;
    private ChessBoard newGame;
    private boolean gameOver;

    public ChessGame() {
        colorTurn = TeamColor.WHITE;
        newGame = new ChessBoard();
        newGame.resetBoard();
        checkCase = false;
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

    public void changeTeamTurn() {
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
        // Check if there is a piece at the start position
        if (newGame.getPiece(startPosition) == null) {
            return null;
        }

        // Get the piece and its possible moves
        ChessPiece piece = newGame.getPiece(startPosition);
        Collection<ChessMove> possibleMoves = piece.pieceMoves(newGame, startPosition);
        ChessGame.TeamColor pieceColor = piece.getTeamColor();

        // Iterate over the possible moves
        Iterator<ChessMove> iterator = possibleMoves.iterator();
        while (iterator.hasNext()) {
            ChessMove move = iterator.next();

            // Create a test game to simulate the move
            ChessGame testGame = new ChessGame();
            ChessBoard testBoard = this.getCopy();
            testGame.isCheckCase();
            testGame.setBoard(testBoard);
            testGame.setTeamTurn(colorTurn);

            try {
                testGame.makeMove(move);
            } catch (InvalidMoveException e) {
                // Remove invalid moves
                iterator.remove();
                continue;
            }

            // Remove moves that leave the player in check
            if (testGame.isInCheck(pieceColor)) {
                iterator.remove();
            }
        }

        return possibleMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (isGameOver()) {
            throw new InvalidMoveException("Game is over, no more moves can be made");
        }
        if (move == null) {
            throw new InvalidMoveException("Invalid move");
        }
        ChessPosition endPos = move.getEndPosition();
        if (endPos.getRow() < 1 || endPos.getRow() > 8 || endPos.getColumn() < 1 || endPos.getColumn() > 8) {
            throw new InvalidMoveException("Invalid move (move is off the board)");
        }
        ChessBoard moveBoard = getCopy();
        ChessPosition startPos = move.getStartPosition();
        ChessPiece piece = moveBoard.getPiece(startPos);

        if (piece == null) {
            throw new InvalidMoveException("There is no piece at the starting position");
        }
        if (!checkCase && piece.getTeamColor() != colorTurn) {
            throw new InvalidMoveException("It is not your turn");
        }
        if (!piece.pieceMoves(moveBoard, startPos).contains(move)) {
            throw new InvalidMoveException("Not a valid move for this piece");

        }

        // Perform the move
        moveBoard.addPiece(startPos, null);
        ChessPiece newPiece = (move.getPromotionPiece() == null) ? piece
                : new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
        moveBoard.addPiece(endPos, newPiece);

        setBoard(moveBoard);

        if (isInCheck(piece.getTeamColor())) {
            throw new InvalidMoveException("Invalid move, this move will put you in check");
        }

        changeTeamTurn();
        if (isInCheckmate(getTeamTurn())) {
            gameOver = true;
        }
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
        AllPiecePositions allPieces = new AllPiecePositions(moveBoard);

        ChessPosition kingPos = (teamColor == TeamColor.WHITE)
                ? allPieces.getWhiteKingPos()
                : allPieces.getBlackKingPos();
        Collection<ChessMove> opponentMoves = (teamColor == TeamColor.WHITE)
                ? allPieces.getBlackTeamMoves()
                : allPieces.getWhiteTeamMoves();

        for (ChessMove move : opponentMoves) {
            if (move.getEndPosition().equals(kingPos)) {
                return true;
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
        if(!isInCheck(teamColor)) {
            return false;
        }
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
        if(isInCheck(teamColor)) {
            return false;
        }
        return anyValidMoves(teamColor);
    }

    public boolean anyValidMoves(TeamColor teamColor) {
        ChessBoard moveBoard = getCopy();
        AllPiecePositions allPieces = new AllPiecePositions(moveBoard);

        Collection<ChessMove> teamMoves = (teamColor == TeamColor.WHITE)
                ? allPieces.getWhiteTeamMoves()
                : allPieces.getBlackTeamMoves();

        for (ChessMove move : teamMoves) {
            ChessGame testMove = new ChessGame();
            ChessBoard testBoard = this.getCopy();
            testMove.isCheckCase();
            testMove.setBoard(testBoard);
            testMove.setTeamTurn(colorTurn);
            try {
                testMove.makeMove(move);
            } catch (InvalidMoveException e) {
                continue;
            }
            if (!testMove.isInCheck(teamColor)) {
                return false;
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