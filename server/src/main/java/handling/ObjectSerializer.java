package handling;

import dataAccess.DataAccessException;
import service.UserService;
import spark.*;

public abstract class ObjectSerializer {
    public abstract String handle(Request request, Response response);

    public String getAuthToken(Request request) {
        return request.headers("authorization");
    }

    public void authorizedCheck(Request request) {
        try {
            if (UserService.validUser(getAuthToken(request)) == null) {
                Spark.halt(401, "{ \"message\": \"Error: unauthorized\" }");
            }
        } catch (DataAccessException e) {
            Spark.halt(500, "{ \"message\": \"Error: " + e.getMessage() + "\" }");
        }
    }
}
