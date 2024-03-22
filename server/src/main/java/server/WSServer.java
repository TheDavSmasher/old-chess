package server;

import org.eclipse.jetty.websocket.api.annotations.*;
import spark.Session;
import spark.Spark;

@WebSocket
public class WSServer {
    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        //TODO
    }
}
