package client.websocket;

import com.google.gson.Gson;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.Session;
import model.response.ErrorResponse;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;

public class WebsocketCommunicator extends Endpoint {
    private final ServerMessageObserver observer;

    public WebsocketCommunicator(ServerMessageObserver messageObserver) {
        observer = messageObserver;
    }

    public void onMessage(String message) {
        try {
            Gson gson = new Gson();
            ServerMessage serverMessage = gson.fromJson(message, Notification.class);
            observer.notify(serverMessage);
        } catch (Exception e) {
            observer.notify(new ErrorMessage(e.getMessage()));
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void joinGame() {}

    public void observeGame() {}
}
