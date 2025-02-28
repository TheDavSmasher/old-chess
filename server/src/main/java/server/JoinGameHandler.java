package server;

import com.google.gson.Gson;
import service.GameService;
import model.request.JoinGameRequest;
import model.response.result.ServiceException;
import spark.Request;

public class JoinGameHandler extends ObjectSerializer {
    @Override
    public String serviceHandle(Request request) throws ServiceException {
        JoinGameRequest joinRequest = new Gson().fromJson(request.body(), JoinGameRequest.class);
        GameService.joinGame(joinRequest, getAuthToken(request));
        return "{}";
    }
}
