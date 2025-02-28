package server;

import service.GameService;
import model.request.CreateGameRequest;
import model.response.CreateGameResponse;
import model.response.result.ServiceException;
import spark.Request;

public class CreateGameHandler extends ObjectSerializer {
    @Override
    public String serviceHandle(Request request) throws ServiceException {
        CreateGameRequest createRequest = gson.fromJson(request.body(), CreateGameRequest.class);
        CreateGameResponse createResponse = GameService.createGame(createRequest, getAuthToken(request));
        return gson.toJson(createResponse);
    }
}
