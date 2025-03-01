package server;

import server.handler.ResponseDeserializer;
import service.GameService;
import model.request.CreateGameRequest;
import model.response.CreateGameResponse;
import model.response.result.ServiceException;

public class CreateGameHandler extends ResponseDeserializer<CreateGameRequest, CreateGameResponse> {
    @Override
    protected CreateGameResponse serviceDeserialize(CreateGameRequest serviceRequest, String authToken) throws ServiceException {
        return GameService.createGame(serviceRequest, authToken);
    }
}
