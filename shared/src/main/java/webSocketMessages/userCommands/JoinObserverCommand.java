package webSocketMessages.userCommands;

public class JoinObserverCommand extends UserGameCommand {
    private final int gameID;
    public JoinObserverCommand(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }
}
