package service;

import dataAccess.DataAccessException;
import model.response.result.ServiceException;

public interface EndpointCall<T> {
    T method() throws ServiceException, DataAccessException;
}
