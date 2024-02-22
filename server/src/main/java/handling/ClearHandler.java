package handling;

import com.google.gson.Gson;
import service.AppService;
import service.result.ServiceException;
import service.result.UnexpectedException;
import spark.Request;
import spark.Response;
import spark.Spark;

public class ClearHandler extends ObjectSerializer {

    @Override
    public String handle(Request request, Response response) {
        Gson gson = new Gson();
        response.type("application/json");
        try {
            AppService.clearData();

        } catch (ServiceException e) {
            Spark.halt(500, "{ \"message\": \"Error: " + e.getMessage() + "\" }");
        }
        response.status(200);
        return "";
    }
}
