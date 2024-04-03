package server;

import server.websocket.WSServer;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/connect", WSServer.class);

        Spark.delete("/db", (request, response) -> (new ClearHandler()).handle(request, response));
        Spark.post("/user", (request, response) -> (new RegisterHandler()).handle(request, response));
        Spark.post("/session", (request, response) -> (new LoginHandler()).handle(request, response));
        Spark.delete("/session", (request, response) -> (new LogoutHandler()).handle(request, response));
        Spark.get("/game", (request, response) -> (new ListGameHandler()).handle(request, response));
        Spark.post("/game", (request, response) -> (new CreateGameHandler()).handle(request, response));
        Spark.put("/game", (request, response) -> (new JoinGameHandler()).handle(request, response));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
