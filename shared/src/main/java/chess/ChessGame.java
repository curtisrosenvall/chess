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
    private TeamColor colorTurn;  // Tracks the current team's turn.
    private boolean checkCase;    // Flag to indicate if the game is currently checking for 'check' conditions.
    private ChessBoard newGame;   // The chessboard associated with the current game.
    private ChessMove lastMove;   // Stores the last move made in the game.

    public ChessGame() {
        colorTurn = TeamColor.WHITE;
        newGame = new ChessBoard();
        newGame.resetBoard();
        checkCase = false;
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
        if(this.colorTurn == TeamColor.WHITE)
            setTeamTurn(TeamColor.BLACK);
        else
            setTeamTurn(TeamColor.WHITE);
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


    /**
     * Calculates and retrieves all valid moves for a piece at the given position.
     * This method also filters out moves that would put or leave one's king in check.
     * @param startPosition The starting position of the piece for which moves are calculated.
     * @return A collection of valid moves for the piece, or null if no piece is present at the starting position.
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        Collection<ChessMove> possibleMoves;

        newGame.setLastMove(lastMove);

        if (newGame.getPiece(startPosition) == null) {
            return null;
        } else {
            possibleMoves = newGame.getPiece(startPosition).pieceMoves(newGame,startPosition);
        }

        ChessGame.TeamColor pieceColor = newGame.getPiece(startPosition).getTeamColor();

        Iterator<ChessMove> i = possibleMoves.iterator();
        while(i.hasNext()) {
            ChessMove move = i.next();
            ChessGame testMove = new ChessGame();
            ChessBoard testBoard = this.getCopy();
            testMove.isCheckCase();
            testMove.setBoard(testBoard);
            testMove.setTeamTurn(colorTurn);
            try {
                testMove.makeMove(move);
            } catch(InvalidMoveException e) {
                i.remove();
                continue;
            }
            if(testMove.isInCheck(pieceColor)) {
                i.remove();
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

    /**
     * Performs a move on the chessboard and updates the game state accordingly.
     * @param move The chess move to perform.
     * @throws InvalidMoveException If the move is invalid.
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(move == null)
            throw new InvalidMoveException();


        ChessPosition checkPos = move.getEndPosition();
        if((checkPos.getRow() < 1) || (checkPos.getRow() > 8) || (checkPos.getColumn() < 1) || (checkPos.getColumn() > 8)) {
            throw new InvalidMoveException();
        }

        ChessBoard ClonedBoard = getCopy();
        ChessPiece piece = ClonedBoard.getPiece(move.getStartPosition());

        if(ClonedBoard.getPiece(move.getStartPosition()) == null)
            throw new InvalidMoveException();

        if(!checkCase && (ClonedBoard.getPiece(move.getStartPosition()).getTeamColor() != colorTurn))
            throw new InvalidMoveException();


        if(piece.pieceMoves(ClonedBoard, move.getStartPosition()).contains(move)) {

            ClonedBoard.addPiece(move.getStartPosition(), null);


            if(move.getPromotionPiece() == null) {
                ClonedBoard.addPiece(move.getEndPosition(), piece);
            }

            else {
                ClonedBoard.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
            }
        } else {
            throw new InvalidMoveException();
        }

        setBoard(ClonedBoard);
        if(!isInCheck(ClonedBoard.getPiece(move.getEndPosition()).getTeamColor())) {
            setBoard(ClonedBoard);
            changeTeamTurn();
        } else
            throw new InvalidMoveException();
        checkCase = false;
        lastMove = move;
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

    /**
     * Determines if any valid moves are available for the specified team. This method is used to check conditions such as
     * stalemate or checkmate by simulating potential moves for all pieces belonging to the team and checking if any of
     * these moves would successfully remove the king from check without placing it back into check.
     *
     * @param teamColor The color of the team (WHITE or BLACK) for which to determine if any valid moves exist.
     * @return false if there is at least one legal move available for the specified team that does not result in the team's
     * king being in check after the move. Returns true if no such moves exist, indicating a potential stalemate or checkmate.
     */

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

    /**
     * Creates and returns a deep copy of the current chess board. This method is essential for game state simulation
     * where changes like potential moves are tested without affecting the actual game state. Cloning the board ensures
     * that all manipulations during these tests do not alter the real game board.
     *
     * @return A deep copy of the current ChessBoard object.
     * @throws RuntimeException if the ChessBoard class does not support cloning, encapsulating the underlying
     * CloneNotSupportedException into a more general unchecked exception that indicates a configuration or coding error.
     */

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

    @Override
    public String toString() {
        return "ChessGame{" +
                "colorTurn=" + colorTurn +
                ", newGame=" + newGame +
                '}';
    }
}