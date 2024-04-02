package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

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

    public Connection getFromUsers(String authToken) { return userConnections.get(authToken); }

    public void sendToConnection(Connection connection, String message) {
        try {
            connection.send(message);
        } catch (IOException ignored) {}
    }
}
