package facade;
import websocket.messages.*;

public interface Watcher {
    void loadGame(LoadGame loadGame);
    void errorMessage(ErrorMessage errorMessage);
    void notification(Notification notification);
}
