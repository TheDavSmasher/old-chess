package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage {
    private final String error;
    public ErrorMessage(String errorMessage) {
        super(ServerMessageType.ERROR);
        error = errorMessage;
    }

    public String getError() {
        return error;
    }
}
