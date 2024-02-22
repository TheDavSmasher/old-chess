package handling;

import com.google.gson.Gson;
import service.GameService;
import service.request.CreateGameRequest;
import service.result.BadRequestException;
import service.result.CreateGameResponse;
import service.result.ServiceException;
import spark.Request;
import spark.Response;
import spark.Spark;

public class CreateGameHandler extends ObjectSerializer {
    @Override
    public String handle(Request request, Response response) {
        authorizedCheck(request);
        Gson gson = new Gson();
        response.type("application/json");
        CreateGameRequest createRequest = gson.fromJson(request.body(), CreateGameRequest.class);
        CreateGameResponse createResponse = null;
        try {
            createResponse = GameService.createGame(createRequest);
        } catch (BadRequestException e) {
            Spark.halt(400, "{ \"message\": \"Error: bad request\" }");
        } catch (ServiceException e) {
            Spark.halt(500, "{ \"message\": \"Error: " + e.getMessage() + "\" }");
        }
        response.status(200);
        return gson.toJson(createResponse);
    }
}
