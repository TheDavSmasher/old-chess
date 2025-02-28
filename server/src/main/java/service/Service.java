package service;

import dataAccess.DataAccessException;
import model.response.result.ServiceException;
import model.response.result.UnexpectedException;

public class Service {
    public static <T> T tryCatch(ServiceCall<T> call) throws ServiceException {
        try {
            return call.method();
        } catch (DataAccessException e) {
            throw new UnexpectedException(e.getMessage());
        }

    }

    public static void tryCatch(EmptyCall call) throws ServiceException {
        try {
            call.method();
        } catch (DataAccessException e) {
            throw new UnexpectedException(e.getMessage());
        }
    }
}
