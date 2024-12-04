package websocket.commands;
import chess.*;

public class MakeMove extends UserGameCommand {

    private final ChessMove move;

    public MakeMove(String authToken, Integer gameID, ChessMove move) {
        super(authToken, gameID);
        this.commandType = CommandType.MAKE_MOVE;
        this.move = move;
    }
    public ChessMove getMove() {
        return move;
    }
}