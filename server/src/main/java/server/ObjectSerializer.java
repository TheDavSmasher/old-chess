package server;

import spark.*;

public abstract class ObjectSerializer {
    public abstract String handle(Request request, Response response);

    public String getAuthToken(Request request) {
        return request.headers("authorization");
    }
}
