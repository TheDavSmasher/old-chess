package service;

import dataAccess.DataAccessException;
import model.response.result.ServiceException;

public interface ServiceCall<T> {
    T method() throws ServiceException, DataAccessException;
}
