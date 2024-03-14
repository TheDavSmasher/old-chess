package server;

import com.google.gson.Gson;
import service.GameService;
import model.request.AuthRequest;
import model.request.JoinGameRequest;
import service.result.BadRequestException;
import service.result.PreexistingException;
import service.result.ServiceException;
import service.result.UnauthorizedException;
import spark.Request;
import spark.Response;
import spark.Spark;

public class JoinGameHandler extends ObjectSerializer {
    @Override
    public String handle(Request request, Response response) {
        Gson gson = new Gson();
        response.type("application/json");
        JoinGameRequest joinRequest = gson.fromJson(request.body(), JoinGameRequest.class);
        AuthRequest authRequest = new AuthRequest(getAuthToken(request));
        try {
            GameService.joinGame(joinRequest, authRequest);
        } catch (BadRequestException e) {
            Spark.halt(400, "{ \"message\": \"Error: bad request\" }");
        } catch (UnauthorizedException e) {
            Spark.halt(401, "{ \"message\": \"Error: unauthorized\" }");
        } catch (PreexistingException e) {
            Spark.halt(403, "{ \"message\": \"Error: already taken\" }");
        } catch (ServiceException e) {
            Spark.halt(500, "{ \"message\": \"Error: " + e.getMessage() + "\" }");
        }
        response.status(200);
        return "{}";
    }
}
