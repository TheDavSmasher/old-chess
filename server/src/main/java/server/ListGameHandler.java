package server;

import service.GameService;
import model.response.ListGamesResponse;
import model.response.result.ServiceException;
import spark.Request;

public class ListGameHandler extends ObjectSerializer<ListGamesResponse> {
    @Override
    public ListGamesResponse serviceHandle(Request request) throws ServiceException {
        return GameService.getAllGames(getAuthToken(request));
    }
}
