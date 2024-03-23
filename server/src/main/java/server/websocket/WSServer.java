package server.websocket;

import com.google.gson.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import spark.Session;
import webSocketMessages.userCommands.*;

import java.lang.reflect.Type;

@WebSocket
public class WSServer {
    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(UserGameCommand.class, CommandDeserializer.class);
        Gson gson = builder.create();
        UserGameCommand gameCommand = gson.fromJson(message, UserGameCommand.class);
        switch (gameCommand.getCommandType()){
            case JOIN_PLAYER -> join((JoinObserverCommand) gameCommand, session);
            case JOIN_OBSERVER -> observe((JoinObserverCommand) gameCommand, session);
            case MAKE_MOVE -> move((MakeMoveCommand) gameCommand, session);
            case LEAVE -> leave((LeaveCommand) gameCommand, session);
            case RESIGN -> resign((ResignCommand) gameCommand, session);
        }
    }

    private void join(JoinObserverCommand command, Session session) {
        ;
    }

    private void observe(JoinObserverCommand command, Session session) {
        ;
    }

    private void move(MakeMoveCommand command, Session session) {
        ;
    }

    private void leave(LeaveCommand command, Session session) {
        ;
    }

    private void resign(ResignCommand command, Session session) {
        ;
    }

    private static class CommandDeserializer implements JsonDeserializer<UserGameCommand> {
        @Override
        public UserGameCommand deserialize(JsonElement jsonElement, Type type,
                                           JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String typeString = jsonObject.get("commandType").getAsString();
            UserGameCommand.CommandType commandType = UserGameCommand.CommandType.valueOf(typeString);

            return switch (commandType) {
                case JOIN_PLAYER -> context.deserialize(jsonObject, JoinPlayerCommand.class);
                case JOIN_OBSERVER -> context.deserialize(jsonObject, JoinObserverCommand.class);
                case MAKE_MOVE -> context.deserialize(jsonObject, MakeMoveCommand.class);
                case LEAVE -> context.deserialize(jsonObject, LeaveCommand.class);
                case RESIGN -> context.deserialize(jsonObject, ResignCommand.class);
            };
        }
    }
}
