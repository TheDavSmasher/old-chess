package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private final ConcurrentHashMap<String, Connection> userConnections = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, ArrayList<Connection>> connectionsToGames = new ConcurrentHashMap<>();

    public void addToUsers(String authToken, String username, Session session) {
        userConnections.put(authToken, new Connection(username, session));
    }

    public void removeFromUser(String authToken) {
        userConnections.remove(authToken);
    }

    public void addToGame(int gameID, Connection connection) {
        ArrayList<Connection> oldConnections = connectionsToGames.get(gameID);
        oldConnections.add(connection);
        connectionsToGames.put(gameID, oldConnections);
    }

    public void removeFromGame(int gameID, Connection connection) {
        ArrayList<Connection> oldConnections = connectionsToGames.get(gameID);
        oldConnections.remove(connection);
        connectionsToGames.put(gameID, oldConnections);
    }
}
