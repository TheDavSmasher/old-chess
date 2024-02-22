package handling;

import spark.*;

public abstract class ObjectSerializer {
    public abstract Response handle(Request r);

    public String getAuthToken(Request r) {
        return r.headers("authorization");
    }
}
