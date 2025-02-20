package model.response.result;

public class BadRequestException extends ServiceException {
    public BadRequestException() {
        super("bad request");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
