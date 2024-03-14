package server;

import com.google.gson.Gson;
import model.response.UserEnterResponse;
import service.UserService;
import model.request.UserEnterRequest;
import service.result.*;
import spark.Request;
import spark.Response;
import spark.Spark;

public class RegisterHandler extends ObjectSerializer {

    @Override
    public String handle(Request request, Response response) {
        Gson gson = new Gson();
        response.type("application/json");
        UserEnterRequest registerRequest = gson.fromJson(request.body(), UserEnterRequest.class);
        UserEnterResponse registerResponse = null;
        try {
            registerResponse = UserService.register(registerRequest);
        } catch (BadRequestException e) {
            Spark.halt(400, "{ \"message\": \"Error: bad request\" }");
        } catch (PreexistingException e) {
            Spark.halt(403, "{ \"message\": \"Error: already taken\" }");
        } catch (ServiceException e) {
            Spark.halt(500, "{ \"message\": \"Error: " + e.getMessage() + "\" }");
        }
        response.status(200);
        return gson.toJson(registerResponse);
    }
}
