package server;

import service.UserService;
import model.request.UserEnterRequest;
import model.response.result.ServiceException;
import model.response.UserEnterResponse;
import spark.Request;

public class LoginHandler extends ObjectSerializer {
    @Override
    public String serviceHandle(Request request) throws ServiceException {
        UserEnterRequest loginRequest = deserialize(request, UserEnterRequest.class);
        UserEnterResponse loginResponse = UserService.login(loginRequest);
        return serialize(loginResponse);
    }
}
