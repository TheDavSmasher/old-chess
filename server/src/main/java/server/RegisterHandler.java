package server;

import model.response.UserEnterResponse;
import model.response.result.ServiceException;
import service.UserService;
import model.request.UserEnterRequest;
import spark.Request;

public class RegisterHandler extends ObjectSerializer {
    @Override
    public String serviceHandle(Request request) throws ServiceException {
        UserEnterRequest registerRequest = deserialize(request, UserEnterRequest.class);
        UserEnterResponse registerResponse = UserService.register(registerRequest);
        return serialize(registerResponse);
    }
}
