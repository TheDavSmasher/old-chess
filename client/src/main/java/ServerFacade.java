import model.*;

import java.util.ArrayList;

public class ServerFacade {

    public static AuthData register(String username, String password, String email) {
        return null;
    }

    public static AuthData login(String username, String password) {
        return null;
    }

    public static ArrayList<GameData> listGames(String authToken) {
        return null;
    }

    public static void createGame(String authToken, String gameName) {
    }

    public static GameData observeGame(String authToken, int gameID) {return null;}

    public static GameData joinGame(String authToken, String color, int gameID) {return null;}

    public static void logout(String authToken) {}
}
