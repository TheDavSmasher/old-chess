package server;

import model.response.UserEnterResponse;
import model.response.result.ServiceException;
import service.UserService;
import model.request.UserEnterRequest;
import spark.Request;

public class RegisterHandler extends ObjectSerializer {
    @Override
    public String serviceHandle(Request request) throws ServiceException {
        UserEnterRequest registerRequest = gson.fromJson(request.body(), UserEnterRequest.class);
        UserEnterResponse registerResponse = UserService.register(registerRequest);
        return gson.toJson(registerResponse);
    }
}
