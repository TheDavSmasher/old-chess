package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private final ConcurrentHashMap<String, Connection> userConnections = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Integer> connectionsToGames = new ConcurrentHashMap<>();

    public void addToUsers(String username, Session session) {
        userConnections.put(username, new Connection(username, session));
    }

    public void removeFromUser(String username) {
        userConnections.remove(username);
    }

    public void addToGame(int gameID, String username) {
        connectionsToGames.put(username, gameID);
    }

    public void removeFromGame(String username) {
        connectionsToGames.remove(username);
    }
}
