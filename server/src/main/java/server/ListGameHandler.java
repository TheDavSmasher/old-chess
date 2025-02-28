package server;

import service.GameService;
import model.response.ListGamesResponse;
import model.response.result.ServiceException;
import spark.Request;

public class ListGameHandler extends ObjectSerializer {
    @Override
    public String serviceHandle(Request request) throws ServiceException {
        ListGamesResponse listResponse = GameService.getAllGames(getAuthToken(request));
        return serialize(listResponse);
    }
}
