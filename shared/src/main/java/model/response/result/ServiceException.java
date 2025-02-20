package model.response.result;

public abstract class ServiceException extends Exception {
    public ServiceException(String message) {
        super(message);
    }

    public String handlerJson() {
        return "{ \"message\": \"Error: " + getMessage() + "\" }";
    }

    public abstract int getStatusCode();
}
