package websocket.messages;

import model.GameData;

public class OnGameLoadMessage extends ServerMessage {

    private GameData game;

    public OnGameLoadMessage(GameData game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public GameData getGame() {
        return game;
    }
}
