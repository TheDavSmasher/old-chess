package server;

import com.google.gson.Gson;
import service.GameService;
import model.request.AuthRequest;
import model.request.CreateGameRequest;
import model.response.result.BadRequestException;
import model.response.CreateGameResponse;
import model.response.result.ServiceException;
import model.response.result.UnauthorizedException;
import spark.Request;
import spark.Response;
import spark.Spark;

public class CreateGameHandler extends ObjectSerializer {
    @Override
    public String serviceHandle(Request request) throws ServiceException {
        Gson gson = new Gson();
        CreateGameRequest createRequest = gson.fromJson(request.body(), CreateGameRequest.class);
        AuthRequest authRequest = new AuthRequest(getAuthToken(request));
        CreateGameResponse createResponse = GameService.createGame(createRequest, authRequest);
        return gson.toJson(createResponse);
    }
}
