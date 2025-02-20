package server;

import com.google.gson.Gson;
import model.response.UserEnterResponse;
import model.response.result.BadRequestException;
import model.response.result.PreexistingException;
import model.response.result.ServiceException;
import service.UserService;
import model.request.UserEnterRequest;
import spark.Request;
import spark.Response;
import spark.Spark;

public class RegisterHandler extends ObjectSerializer {
    @Override
    public String serviceHandle(Request request) throws ServiceException {
        Gson gson = new Gson();
        UserEnterRequest registerRequest = gson.fromJson(request.body(), UserEnterRequest.class);
        UserEnterResponse registerResponse = UserService.register(registerRequest);
        return gson.toJson(registerResponse);
    }
}
