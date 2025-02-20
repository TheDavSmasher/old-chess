package server;

import com.google.gson.Gson;
import service.GameService;
import model.request.AuthRequest;
import model.request.JoinGameRequest;
import model.response.result.BadRequestException;
import model.response.result.PreexistingException;
import model.response.result.ServiceException;
import model.response.result.UnauthorizedException;
import spark.Request;
import spark.Response;
import spark.Spark;

public class JoinGameHandler extends ObjectSerializer {
    @Override
    public String serviceHandle(Request request) throws ServiceException {
        JoinGameRequest joinRequest = new Gson().fromJson(request.body(), JoinGameRequest.class);
        AuthRequest authRequest = new AuthRequest(getAuthToken(request));
        GameService.joinGame(joinRequest, authRequest);
        return "{}";
    }
}
