package server;

import model.response.result.ServiceException;
import spark.*;

public abstract class ObjectSerializer implements Route {
    public String handle(Request request, Response response) {
        response.type("application/json");
        String result = null;
        try {
            result = serviceHandle(request);
        } catch (ServiceException e) {
            Spark.halt(e.getStatusCode(), e.handlerJson());
        }
        response.status(200);
        return result;
    }

    public abstract String serviceHandle(Request request) throws ServiceException;

    public String getAuthToken(Request request) {
        return request.headers("authorization");
    }
}
