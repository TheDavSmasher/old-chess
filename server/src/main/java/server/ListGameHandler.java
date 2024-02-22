package server;

import com.google.gson.Gson;
import service.GameService;
import service.result.ListGamesResponse;
import service.result.ServiceException;
import spark.Request;
import spark.Response;
import spark.Spark;

public class ListGameHandler extends ObjectSerializer {

    @Override
    public String handle(Request request, Response response) {
        authorizedCheck(request);
        Gson gson = new Gson();
        response.type("application/json");
        ListGamesResponse listResponse = null;
        try {
            listResponse = GameService.getAllGames();
        } catch (ServiceException e) {
            Spark.halt(500, "{ \"message\": \"Error: " + e.getMessage() + "\" }");
        }
        response.status(200);
        return gson.toJson(listResponse);
    }
}
