package server.websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.*;
import model.dataAccess.AuthData;
import model.response.result.ServiceException;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.Session;
import service.GameService;
import service.UserService;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.lang.reflect.Type;

@WebSocket
public class WSServer {
    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        Gson gson = new Gson();
        UserGameCommand gameCommand = gson.fromJson(message, UserGameCommand.class);
        switch (gameCommand.getCommandType()){
            case JOIN_PLAYER -> join(gson.fromJson(message, JoinPlayerCommand.class), session);
            case JOIN_OBSERVER -> observe(gson.fromJson(message, JoinObserverCommand.class), session);
            case MAKE_MOVE -> move(gson.fromJson(message, MakeMoveCommand.class), session);
            case LEAVE -> leave(gson.fromJson(message, LeaveCommand.class), session);
            case RESIGN -> resign(gson.fromJson(message, ResignCommand.class), session);
        }
    }

    private final ConnectionManager connectionManager = new ConnectionManager();

    private void join(JoinPlayerCommand command, Session session) {
        try {
            String username = enter(command.getAuthString(), command.getGameID(), session);
            Notification notification = new Notification(username + " has joined the game as " + command.getColor() + ".");
            connectionManager.notifyOthers(command.getGameID(), command.getAuthString(), notification);
        } catch (ServiceException e) {
            sendError(session, e.getMessage());
        }
    }

    private void observe(JoinObserverCommand command, Session session) {
        try {
            String username = enter(command.getAuthString(), command.getGameID(), session);
            Notification notification = new Notification(username + " is now observing the game.");
            connectionManager.notifyOthers(command.getGameID(), command.getAuthString(), notification);
        } catch (ServiceException e) {
            sendError(session, e.getMessage());
        }
    }

    private String enter(String authToken, int gameID, Session session) throws ServiceException {
        AuthData auth = UserService.getUser(authToken);
        if (auth == null) {
            throw new ServiceException("You are unauthorized.");
        }
        connectionManager.addToGame(gameID, authToken, auth.username(), session);
        connectionManager.loadNewGame(GameService.getGame(authToken, gameID).game(), authToken);
        return auth.username();
    }

    private void move(MakeMoveCommand command, Session session) {
        try {
            Connection connection = connectionManager.getFromUsers(command.getAuthString());
            if (connection == null) {
                sendError(session, "You are unauthorized");
                return;
            }
            ChessGame game = GameService.getGame(command.getAuthString(), command.getGameID()).game();
            game.makeMove(command.getMove());
            String gameJson = new Gson().toJson(game);
            GameService.updateGameState(command.getAuthString(), command.getGameID(), gameJson);
            connectionManager.loadNewGame(game, command.getGameID());
        } catch (ServiceException | InvalidMoveException e) {
            sendError(session, e.getMessage());
        }
    }

    private void leave(LeaveCommand command, Session session) {
        try {
            Connection connection = connectionManager.getFromUsers(command.getAuthString());
            if (connection == null) {
                sendError(session, "You are unauthorized");
                return;
            }
            GameService.leaveGame(command.getAuthString(), command.getGameID());
            connectionManager.removeFromGame(command.getGameID(), command.getAuthString());
            Notification notification = new Notification(connection.username + " has left the game.");
            connectionManager.notifyOthers(command.getGameID(), command.getAuthString(), notification);
        } catch (ServiceException e) {
            sendError(session, e.getMessage());
        }
    }

    private void resign(ResignCommand command, Session session) {
        try {
            Connection connection = connectionManager.getFromUsers(command.getAuthString());
            if (connection == null) {
                sendError(session, "You are unauthorized");
                return;
            }
            GameService.leaveGame(command.getAuthString(), command.getGameID());
            ChessGame gameResigned = GameService.getGame(command.getAuthString(), command.getGameID()).game();
            gameResigned.endGame();
            String gameJson = new Gson().toJson(gameResigned);
            GameService.updateGameState(command.getAuthString(), command.getGameID(), gameJson);
            connectionManager.removeFromGame(command.getGameID(), command.getAuthString());
            Notification notification = new Notification(connection.username + " has resigned the game.");
            connectionManager.notifyOthers(command.getGameID(), command.getAuthString(), notification);
        } catch (ServiceException e) {
            sendError(session, e.getMessage());
        }
    }

    private void sendError(Session session, String message) {
        try {
            ErrorMessage error = new ErrorMessage(message);
            session.getRemote().sendString(new Gson().toJson(error));
        } catch (IOException ignored) {}
    }
}
