package service;

import dataAccess.*;
import service.result.UnexpectedException;
import service.result.ServiceException;

public class AppService {
    public static void clearData() throws ServiceException {
        try {
            AuthDAO authDAO = SQLAuthDAO.getInstance();
            UserDAO userDAO = SQLUserDAO.getInstance();
            GameDAO gameDAO = SQLGameDAO.getInstance();

            authDAO.clear();
            userDAO.clear();
            gameDAO.clear();
        } catch (DataAccessException e) {
            throw new UnexpectedException(e.getMessage());
        }
    }
}
