package service.result;

public class CreateGameResponse extends Response {
    private final int gameID;

    public CreateGameResponse(int status, int gameID) {
        super(status);
        this.gameID = gameID;
    }
}
