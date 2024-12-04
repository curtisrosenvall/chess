package facade;

import javax.websocket.*;
import java.net.URI;

public class WebSockets extends Endpoint {
    public Sesssion session;

    public WebSockets(Watcher watcher) throws Exception {
        URI uri = new URI("ws://localhost:8080/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);
    }
}

