package server;

import com.google.gson.Gson;
import service.GameService;
import service.request.AuthRequest;
import service.request.JoinGameRequest;
import service.result.BadRequestException;
import service.result.PreexistingException;
import service.result.ServiceException;
import spark.Request;
import spark.Response;
import spark.Spark;

public class JoinGameHandler extends ObjectSerializer {
    @Override
    public String handle(Request request, Response response) {
        authorizedCheck(request);
        Gson gson = new Gson();
        response.type("application/json");
        JoinGameRequest joinRequest = gson.fromJson(request.body(), JoinGameRequest.class);
        AuthRequest authRequest = new AuthRequest(getAuthToken(request));
        try {
            GameService.joinGame(joinRequest, authRequest);
        } catch (BadRequestException e) {
            Spark.halt(400, "{ \"message\": \"Error: bad request\" }");
        } catch (PreexistingException e) {
            Spark.halt(403, "{ \"message\": \"Error: already taken\" }");
        } catch (ServiceException e) {
            Spark.halt(500, "{ \"message\": \"Error: " + e.getMessage() + "\" }");
        }
        response.status(200);
        return "{}";
    }
}
