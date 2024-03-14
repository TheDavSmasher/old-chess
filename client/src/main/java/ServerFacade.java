import com.google.gson.Gson;
import model.dataAccess.AuthData;
import model.dataAccess.GameData;
import model.dataAccess.UserData;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerFacade {

    private static final String urlPort = "http://localhost:8080/";

    public static AuthData register(String username, String password, String email) {
        String body = new Gson().toJson(new UserData(username, password, email));
        var map = ClientCommunicator.doPost(urlPort + "user", body, null);

        return null;
    }

    public static AuthData login(String username, String password) {
        String body = new Gson().toJson(new HashMap<String, String>());

        var map = ClientCommunicator.doPost(urlPort + "session", body, null);

        return null;
    }

    public static ArrayList<GameData> listGames(String authToken) {
        var map = ClientCommunicator.doGet(urlPort + "game", authToken);
        return null;
    }

    public static void createGame(String authToken, String gameName) {
        String body = new Gson().toJson();
        var map = ClientCommunicator.doPost(urlPort + "game", body, authToken);
    }

    public static GameData observeGame(String authToken, int gameID) {return null;}

    public static GameData joinGame(String authToken, String color, int gameID) {return null;}

    public static void logout(String authToken) {}
}
