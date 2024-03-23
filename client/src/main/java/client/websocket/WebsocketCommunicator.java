package client.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.*;
import jakarta.websocket.*;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.lang.reflect.Type;
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
                        GsonBuilder builder = new GsonBuilder();
                        builder.registerTypeAdapter(ServerMessage.class, new NotificationDeserializer());
                        Gson gson = builder.create();
                        ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
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

    public void joinGame(String authToken, int gameID, String color) {
        ChessGame.TeamColor teamColor = ChessGame.TeamColor.valueOf(color.toUpperCase());
        JoinPlayerCommand command = new JoinPlayerCommand(authToken, gameID, teamColor);
    }

    public void observeGame(String authToken, int gameID) {
        JoinObserverCommand command = new JoinObserverCommand(authToken, gameID);
    }

    public void makeMove(String authToken, int gameID, ChessMove move) {
        MakeMoveCommand command = new MakeMoveCommand(authToken, gameID, move);
    }

    public void leaveGame(String authToken, int gameID) {
        LeaveCommand command = new LeaveCommand(authToken, gameID);
    }

    public void resignGame(String authToken, int gameID) {
        ResignCommand command = new ResignCommand(authToken, gameID);
    }

    private static class NotificationDeserializer implements JsonDeserializer<ServerMessage> {
        @Override
        public Notification deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String typeString = jsonObject.get("serverMessageType").getAsString();
            ServerMessage.ServerMessageType messageType = ServerMessage.ServerMessageType.valueOf(typeString);

            return switch (messageType) {
                case NOTIFICATION -> context.deserialize(jsonElement, Notification.class);
                case LOAD_GAME -> context.deserialize(jsonElement, LoadGameMessage.class);
                case ERROR -> context.deserialize(jsonElement, ErrorMessage.class);
            };
        }
    }
}
