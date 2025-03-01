package server.handler;

import com.google.gson.Gson;
import model.response.result.ServiceException;
import spark.*;

public abstract class ObjectSerializer<T> implements Route {
    protected final Gson gson = new Gson();

    public String handle(Request request, Response response) {
        response.type("application/json");
        String result = null;
        try {
            T serviceResponse = serviceHandle(request);
            result = serialize(serviceResponse);
        } catch (ServiceException e) {
            Spark.halt(e.getStatusCode(), e.handlerJson());
        }
        response.status(200);
        return result;
    }

    protected abstract T serviceHandle(Request request) throws ServiceException;

    protected String serialize(Object object) {
        return gson.toJson(object);
    }

    protected String getAuthToken(Request request) {
        return request.headers("authorization");
    }
}
