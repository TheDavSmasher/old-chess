package handling;

import service.UserService;
import service.request.AuthRequest;
import service.result.ServiceException;
import spark.Request;
import spark.Response;
import spark.Spark;

public class LogoutHandler extends ObjectSerializer {

    @Override
    public String handle(Request request, Response response) {
        authorizedCheck(request);
        response.type("application/json");
        AuthRequest logoutRequest = new AuthRequest(getAuthToken(request));
        try {
            UserService.logout(logoutRequest);
        } catch (ServiceException e) {
            Spark.halt(500, "{ \"message\": \"Error: " + e.getMessage() + "\" }");
        }
        response.status(200);
        return "{}";
    }
}
