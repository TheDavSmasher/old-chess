package server;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.*;
import spark.Session;
import webSocketMessages.userCommands.*;

@WebSocket
public class WSServer {
    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        //TODO type adapter
        UserGameCommand gameCommand = new Gson().fromJson(message, UserGameCommand.class);
        switch (gameCommand.getCommandType()){
            case JOIN_PLAYER -> join((JoinObserverCommand) gameCommand);
            case JOIN_OBSERVER -> observe((JoinObserverCommand) gameCommand);
            case MAKE_MOVE -> move((MakeMoveCommand) gameCommand);
            case LEAVE -> leave((LeaveCommand) gameCommand);
            case RESIGN -> resign((ResignCommand) gameCommand);
        }
    }

    private void join(JoinObserverCommand command) {
        ;
    }

    private void observe(JoinObserverCommand command) {
        ;
    }

    private void move(MakeMoveCommand command) {
        ;
    }

    private void leave(LeaveCommand command) {
        ;
    }

    private void resign(ResignCommand command) {
        ;
    }
}
