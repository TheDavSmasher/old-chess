package service.result;

public class UserEnterResponse extends Response {
    private final String username;
    private final String authToken;

    public UserEnterResponse(int status, String username, String authToken) {
        super(status);
        this.username = username;
        this.authToken = authToken;
    }
}
