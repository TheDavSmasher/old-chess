package service;

import dataAccess.DataAccessException;
import model.response.result.ServiceException;
import model.response.result.UnexpectedException;

public class Service {
    public static final boolean UseSQL = true;

    public static <T> T tryCatch(EndpointCall<T> call) throws ServiceException {
        try {
            return call.method();
        } catch (DataAccessException e) {
            throw new UnexpectedException(e.getMessage());
        }

    }

    public static void tryCatch(WebSocketCall call) throws ServiceException {
        try {
            call.method();
        } catch (DataAccessException e) {
            throw new UnexpectedException(e.getMessage());
        }
    }
}
