package server;

import handling.*;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.delete("/clear", (request, response) -> (new ClearHandler()).handle(request));
        Spark.post("/user", (request, response) -> (new RegisterHandler()).handle(request));
        Spark.post("/session", (request, response) -> (new LoginHandler()).handle(request));
        Spark.delete("/session", (request, response) -> (new LogoutHandler()).handle(request));
        Spark.get("/game", (request, response) -> (new ListGameHandler()).handle(request));
        Spark.post("/game", (request, response) -> (new CreateGameHandler()).handle(request));
        Spark.put("/game", (request, response) -> (new JoinGameHandler()).handle(request));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
