package server.handler;

import model.response.EmptyResponse;
import service.UserService;
import model.response.result.ServiceException;
import spark.Request;

public class LogoutHandler extends ObjectSerializer<EmptyResponse> {
    @Override
    public EmptyResponse serviceHandle(Request request) throws ServiceException {
        return UserService.logout(getAuthToken(request));
    }
}
