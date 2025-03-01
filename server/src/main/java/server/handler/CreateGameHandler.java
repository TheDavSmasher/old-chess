package server.handler;

import service.GameService;
import model.request.CreateGameRequest;
import model.response.CreateGameResponse;
import model.response.result.ServiceException;

public class CreateGameHandler extends RequestDeserializer<CreateGameRequest, CreateGameResponse> {
    @Override
    protected CreateGameResponse serviceCall(CreateGameRequest createGameRequest, String authToken) throws ServiceException {
        return GameService.createGame(createGameRequest, authToken);
    }
}
