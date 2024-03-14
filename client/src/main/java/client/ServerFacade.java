package client;

import com.google.gson.Gson;
import model.dataAccess.GameData;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.UserEnterRequest;
import model.response.CreateGameResponse;
import model.response.EmptyResponse;
import model.response.ListGamesResponse;
import model.response.UserEnterResponse;

import java.io.IOException;
import java.util.ArrayList;

public class ServerFacade {

    private static String urlPort = "http://localhost:8080/";

    public ServerFacade(int port) {
        urlPort = "http://localhost:" + port + "/";
    }

    public static UserEnterResponse register(String username, String password, String email) throws IOException {
        String url = urlPort + "user";
        String body = new Gson().toJson(new UserEnterRequest(username, password, email));
        return ClientCommunicator.doPost(url, body, null, UserEnterResponse.class);
    }

    public static UserEnterResponse login(String username, String password) throws IOException {
        String url = urlPort + "session";
        String body = new Gson().toJson(new UserEnterRequest(username, password, null));
        return ClientCommunicator.doPost(url, body, null, UserEnterResponse.class);
    }

    public static ArrayList<GameData> listGames(String authToken) throws IOException {
        ListGamesResponse response = ClientCommunicator.doGet(urlPort + "game", authToken, ListGamesResponse.class);
        return response.games();
    }

    public static CreateGameResponse createGame(String authToken, String gameName) throws IOException {
        String url = urlPort + "game";
        String body = new Gson().toJson(new CreateGameRequest(gameName));
        return ClientCommunicator.doPost(url, body, authToken, CreateGameResponse.class);
    }

    public static void observeGame(String authToken, int gameID) throws IOException {
        String url = urlPort + "game";
        String body = new Gson().toJson(new JoinGameRequest(null, gameID));
        ClientCommunicator.doPut(url, body, authToken, EmptyResponse.class);
    }

    public static void joinGame(String authToken, String color, int gameID) throws IOException {
        String url = urlPort + "game";
        String body = new Gson().toJson(new JoinGameRequest(color, gameID));
        ClientCommunicator.doPut(url, body, authToken, EmptyResponse.class);
    }

    public static void logout(String authToken) throws IOException {
        String url = urlPort + "session";
        ClientCommunicator.doDelete(url, authToken, EmptyResponse.class);
    }
}
