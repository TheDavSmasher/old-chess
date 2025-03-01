package server.handler;

import service.GameService;
import model.request.CreateGameRequest;
import model.response.CreateGameResponse;
import model.response.result.ServiceException;

public class CreateGameHandler extends RequestDeserializer<CreateGameRequest, CreateGameResponse> {
    @Override
    protected CreateGameResponse serviceCall(CreateGameRequest serviceRequest, String authToken) throws ServiceException {
        return GameService.createGame(serviceRequest, authToken);
    }
}
