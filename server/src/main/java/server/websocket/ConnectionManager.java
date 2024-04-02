package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.Notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private final ConcurrentHashMap<String, Connection> userConnections = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, ArrayList<Connection>> connectionsToGames = new ConcurrentHashMap<>();

    private void addToUsers(String authToken, Connection connection) {
        userConnections.put(authToken, connection);
    }

    private void removeFromUser(String authToken) {
        userConnections.remove(authToken);
    }

    public void addToGame(int gameID, String authToken, String username, Session session) {
        Connection newConnection = new Connection(username, session);
        ArrayList<Connection> oldConnections = connectionsToGames.get(gameID);
        if (oldConnections == null) {
            connectionsToGames.put(gameID, new ArrayList<>());
            oldConnections = connectionsToGames.get(gameID);
        }
        oldConnections.add(newConnection);
        connectionsToGames.put(gameID, oldConnections);
        addToUsers(authToken, newConnection);
    }

    public void removeFromGame(int gameID, String authToken) {
        ArrayList<Connection> oldConnections = connectionsToGames.get(gameID);
        oldConnections.remove(getFromUsers(authToken));
        connectionsToGames.put(gameID, oldConnections);
        removeFromUser(authToken);
    }

    public Connection getFromUsers(String authToken) {
        return userConnections.get(authToken);
    }

    public void loadNewGame(ChessGame game, int gameID) {
        Gson gson = new Gson();
        String gameJson = gson.toJson(game);
        String message = gson.toJson(new LoadGameMessage(gameJson));
        for (Connection current : connectionsToGames.get(gameID)) {
            sendToConnection(current, message);
        }
    }

    public void loadNewGame(ChessGame game, String authToken) {
        Gson gson = new Gson();
        String gameJson = gson.toJson(game);
        String message = gson.toJson(new LoadGameMessage(gameJson));
        sendToConnection(userConnections.get(authToken), message);
    }

    public void notifyOthers(int gameID, String authToken, Notification notification) {
        ArrayList<Connection> gameConnections = connectionsToGames.get(gameID);
        gameConnections.remove(userConnections.get(authToken));
        String message = new Gson().toJson(notification);
        for (Connection current : gameConnections) {
            sendToConnection(current, message);
        }
    }

    public void sendToConnection(Connection connection, String message) {
        try {
            connection.send(message);
        } catch (IOException ignored) {}
    }
}
