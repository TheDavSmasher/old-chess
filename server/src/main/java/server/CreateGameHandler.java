package server;

import com.google.gson.Gson;
import service.GameService;
import model.request.AuthRequest;
import model.request.CreateGameRequest;
import model.response.result.BadRequestException;
import model.response.CreateGameResponse;
import model.response.result.ServiceException;
import model.response.result.UnauthorizedException;
import spark.Request;
import spark.Response;
import spark.Spark;

public class CreateGameHandler extends ObjectSerializer {
    @Override
    public String handle(Request request, Response response) {
        Gson gson = new Gson();
        response.type("application/json");
        CreateGameRequest createRequest = gson.fromJson(request.body(), CreateGameRequest.class);
        AuthRequest authRequest = new AuthRequest(getAuthToken(request));
        CreateGameResponse createResponse = null;
        try {
            createResponse = GameService.createGame(createRequest, authRequest);
        } catch (BadRequestException e) {
            Spark.halt(400, "{ \"message\": \"Error: bad request\" }");
        } catch (UnauthorizedException e) {
            Spark.halt(401, "{ \"message\": \"Error: unauthorized\" }");
        } catch (ServiceException e) {
            Spark.halt(500, "{ \"message\": \"Error: " + e.getMessage() + "\" }");
        }
        response.status(200);
        return gson.toJson(createResponse);
    }
}
