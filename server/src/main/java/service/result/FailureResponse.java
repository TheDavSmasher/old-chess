package service.result;

public class FailureResponse extends Response {
    private final String message;

    public FailureResponse(int status, String message) {
        super(status);
        this.message = message;
    }
}
