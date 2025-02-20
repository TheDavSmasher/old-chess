package model.response.result;

public class UnauthorizedException extends ServiceException {
    public UnauthorizedException() {
        super("unauthorized");
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
