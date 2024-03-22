package server;

import com.google.gson.Gson;
import service.GameService;
import model.request.AuthRequest;
import model.response.ListGamesResponse;
import model.response.result.ServiceException;
import model.response.result.UnauthorizedException;
import spark.Request;
import spark.Response;
import spark.Spark;

public class ListGameHandler extends ObjectSerializer {

    @Override
    public String handle(Request request, Response response) {
        Gson gson = new Gson();
        response.type("application/json");
        AuthRequest authRequest = new AuthRequest(getAuthToken(request));
        ListGamesResponse listResponse = null;
        try {
            listResponse = GameService.getAllGames(authRequest);
        } catch (UnauthorizedException e) {
            Spark.halt(401, "{ \"message\": \"Error: unauthorized\" }");
        } catch (ServiceException e) {
            Spark.halt(500, "{ \"message\": \"Error: " + e.getMessage() + "\" }");
        }
        response.status(200);
        return gson.toJson(listResponse);
    }
}
