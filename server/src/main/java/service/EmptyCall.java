package service;

import dataAccess.DataAccessException;
import model.response.result.ServiceException;

public interface EmptyCall {
    void method() throws ServiceException, DataAccessException;
}
