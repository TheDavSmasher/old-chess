package handling;

import service.AppService;
import service.result.ServiceException;
import spark.Request;
import spark.Response;
import spark.Spark;

public class ClearHandler extends ObjectSerializer {

    @Override
    public String handle(Request request, Response response) {
        response.type("application/json");
        try {
            AppService.clearData();

        } catch (ServiceException e) {
            Spark.halt(500, "{ \"message\": \"Error: " + e.getMessage() + "\" }");
        }
        response.status(200);
        return "{}";
    }
}
