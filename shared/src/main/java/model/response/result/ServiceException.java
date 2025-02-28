package model.response.result;

public class ServiceException extends Exception {
    public ServiceException(String message) {
        super(message);
    }

    public String handlerJson() {
        return "{ \"message\": \"Error: " + getMessage() + "\" }";
    }

    public int getStatusCode() {
        return 500;
    }
}
