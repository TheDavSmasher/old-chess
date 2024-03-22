package client.websocket;

import com.google.gson.Gson;
import model.response.ErrorResponse;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.ServerMessage;

public class WebsocketCommunicator {
    private ServerMessageObserver observer;

    public void onMessage(String message) {
        try {
            Gson gson = new Gson();
            ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
            observer.notify(serverMessage);
        } catch (Exception e) {
            observer.notify(new ErrorMessage(e.getMessage()));
        }
    }
}
