package model.response.result;

public class UnexpectedException extends ServiceException {
    public UnexpectedException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return 500;
    }
}
