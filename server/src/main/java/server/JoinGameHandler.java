package server;

import service.GameService;
import model.request.JoinGameRequest;
import model.response.result.ServiceException;
import spark.Request;

public class JoinGameHandler extends ObjectSerializer {
    @Override
    public String serviceHandle(Request request) throws ServiceException {
        JoinGameRequest joinRequest = deserialize(request, JoinGameRequest.class);
        GameService.joinGame(joinRequest, getAuthToken(request));
        return "{}";
    }
}
