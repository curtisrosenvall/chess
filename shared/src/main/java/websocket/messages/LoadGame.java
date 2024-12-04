package websocket.messages;

import chess.ChessGame;
import models.GameData;

public class LoadGame extends ServerMessage {

    private GameData game;

    public LoadGame(GameData game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public GameData getGame() {
        return game;
    }
}