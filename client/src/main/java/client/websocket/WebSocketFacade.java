package client.websocket;

import com.sun.nio.sctp.NotificationHandler;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.Session;

public class WebSocketFacade extends Endpoint {
    private Session session;
    private NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) {

    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
