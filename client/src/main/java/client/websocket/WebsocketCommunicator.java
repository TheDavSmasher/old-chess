package client.websocket;

import com.google.gson.Gson;
import jakarta.websocket.*;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.net.URI;

public class WebsocketCommunicator extends Endpoint {
    private final Session session;
    private final ServerMessageObserver observer;

    public WebsocketCommunicator(String url, ServerMessageObserver messageObserver) throws IOException {
        try {
            observer = messageObserver;
            url = url.replace("http", "ws");
            URI socketURI = URI.create(url + "/connect");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(this, socketURI);

            session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    try {
                        //TODO type adapt
                        ServerMessage serverMessage = new Gson().fromJson(message, Notification.class);
                        observer.notify(serverMessage);
                    } catch (Exception e) {
                        observer.notify(new ErrorMessage(e.getMessage()));
                    }
                }
            });
        } catch (DeploymentException | IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void joinGame() {}

    public void observeGame() {}
}
