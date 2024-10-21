package server;

import spark.*;

public abstract class ObjectSerializer implements Route {
    public abstract String handle(Request request, Response response);

    public String getAuthToken(Request request) {
        return request.headers("authorization");
    }
}
