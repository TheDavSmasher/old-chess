import model.*;

import java.util.ArrayList;

public class ServerFacade {

    private final int port;

    public ServerFacade(int port) {
        this.port = port;
    }

    public AuthData register(String username, String password, String email) {
        return null;
    }

    public AuthData login(String username, String password) {
        return null;
    }

    public ArrayList<GameData> listGames(String authToken) {
        return null;
    }

    public int createGame(String authToken, String gameName) {
        return 0;
    }

    public void observeGame(String authToken, int gameID) {}

    public void joinGame(String authToken, String color, int gameID) {}

    public void logout(String authToken) {}
}
