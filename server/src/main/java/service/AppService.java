package service;

import dataAccess.*;
import service.result.ServiceException;

public class AppService {
    public static void clearData() throws ServiceException {
        try {
            AuthDAO authDAO = MemoryAuthDAO.getInstance();
            UserDAO userDAO = MemoryUserDAO.getInstance();
            GameDAO gameDAO = MemoryGameDAO.getInstance();

            authDAO.clear();
            userDAO.clear();
            gameDAO.clear();
        } catch (DataAccessException e) {
            throw new ServiceException("Error: " + e.getMessage());
        }
    }
}
