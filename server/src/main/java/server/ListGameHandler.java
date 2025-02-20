package server;

import com.google.gson.Gson;
import service.GameService;
import model.request.AuthRequest;
import model.response.ListGamesResponse;
import model.response.result.ServiceException;
import model.response.result.UnauthorizedException;
import spark.Request;
import spark.Response;
import spark.Spark;

public class ListGameHandler extends ObjectSerializer {
    @Override
    public String serviceHandle(Request request) throws ServiceException {
        AuthRequest authRequest = new AuthRequest(getAuthToken(request));
        ListGamesResponse listResponse = GameService.getAllGames(authRequest);
        return new Gson().toJson(listResponse);
    }
}
