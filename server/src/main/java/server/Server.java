package server;

import server.handler.*;
import server.websocket.WSServer;
import service.UserService;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/connect", WSServer.class);

        Spark.delete("/db", new ClearHandler());
        Spark.post("/user", new UserEnterHandler(UserService::register));
        Spark.post("/session", new UserEnterHandler(UserService::login));
        Spark.delete("/session", new LogoutHandler());
        Spark.get("/game", new ListGameHandler());
        Spark.post("/game", new CreateGameHandler());
        Spark.put("/game", new JoinGameHandler());

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
