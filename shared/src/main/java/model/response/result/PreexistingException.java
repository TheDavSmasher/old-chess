package model.response.result;

public class PreexistingException extends ServiceException {
    public PreexistingException() {
        super("already taken");
    }

    @Override
    public int getStatusCode() {
        return 403;
    }
}
