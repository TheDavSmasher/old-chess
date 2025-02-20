package server;

import com.google.gson.Gson;
import service.UserService;
import model.request.UserEnterRequest;
import model.response.result.ServiceException;
import model.response.result.UnauthorizedException;
import model.response.UserEnterResponse;
import spark.Request;
import spark.Response;
import spark.Spark;

public class LoginHandler extends ObjectSerializer {
    @Override
    public String serviceHandle(Request request) throws ServiceException {
        Gson gson = new Gson();
        UserEnterRequest loginRequest = gson.fromJson(request.body(), UserEnterRequest.class);
        UserEnterResponse loginResponse = UserService.login(loginRequest);
        return gson.toJson(loginResponse);
    }
}
