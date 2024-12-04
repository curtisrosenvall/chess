package facade;
import javax.websocket.Session;
import javax.websocket.*;
import java.net.URI;
import com.google.gson.Gson;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

public class WebSockets extends Endpoint {

    public Session session;

    public WebSockets(Watcher watcher) throws Exception {
        URI uri = new URI("ws://localhost:8080/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {


                try {
                    ServerMessage myMessage = new Gson().fromJson(message, ServerMessage.class);
                    switch (myMessage.getServerMessageType()) {
                        case LOAD_GAME -> watcher.loadGame(new Gson().fromJson(message, LoadGame.class));
                        case ERROR -> watcher.errorMessage(new Gson().fromJson(message, ErrorMessage.class));
                        case NOTIFICATION -> watcher.notification(new Gson().fromJson(message, Notification.class));
                    }

                    //System.out.println(message);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}

}

