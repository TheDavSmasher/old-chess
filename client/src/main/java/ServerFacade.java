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

    public static int createGame(String authToken, String gameName) {
        return 0;
    }

    public static void observeGame(String authToken, int gameID) {}

    public static void joinGame(String authToken, String color, int gameID) {}

    public static void logout(String authToken) {}
}
