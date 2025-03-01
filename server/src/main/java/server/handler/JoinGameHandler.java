package server.handler;

import service.GameService;
import model.response.EmptyResponse;
import model.request.JoinGameRequest;
import model.response.result.ServiceException;

public class JoinGameHandler extends ResponseDeserializer<JoinGameRequest, EmptyResponse> {
    @Override
    protected EmptyResponse serviceCall(JoinGameRequest serviceRequest, String authToken) throws ServiceException {
        return GameService.joinGame(serviceRequest, authToken);
    }
}
