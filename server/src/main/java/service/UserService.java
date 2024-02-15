package service;

import dataAccess.*;
import dataModels.authData;
import dataModels.userData;
import service.request.AuthRequest;
import service.result.UserEnterResponse;
import service.result.ServiceException;

public class UserService {
    public static UserEnterResponse register(userData request) throws ServiceException {
        try {
            UserDAO userDAO = MemoryUserDAO.getInstance();

            if (request.username().isEmpty() || request.password().isEmpty() || request.email().isEmpty()) {
                throw new ServiceException("Error: bad request");
            }
            if (userDAO.getUser(request.username()) != null) {
                throw new ServiceException("Error: already taken");
            }
            userDAO.createUser(request.username(), request.password(), request.email());
            return UserService.login(request);
        } catch (DataAccessException e) {
            throw new ServiceException( "Error: " + e.getMessage());
        }
    }

    public static UserEnterResponse login(userData request) throws ServiceException {
        try {
            UserDAO userDAO = MemoryUserDAO.getInstance();
            AuthDAO authDAO = MemoryAuthDAO.getInstance();

            if (userDAO.getUser(request.username()) == null) {
                throw new ServiceException("Error: unauthorized");
            }
            authData newAuth = authDAO.createAuth(request.username());
            return new UserEnterResponse(newAuth.username(), newAuth.authToken());
        } catch (DataAccessException e) {
            throw new ServiceException( "Error: " + e.getMessage());
        }
    }

    public static void logout(AuthRequest request) throws ServiceException {
        try {
            AuthDAO authDAO = MemoryAuthDAO.getInstance();

            if (UserService.validUser(request.authToken()) == null) {
                throw new ServiceException("Error: unauthorized");
            }
            authDAO.deleteAuth(request.authToken());
        } catch (DataAccessException e) {
            throw new ServiceException( "Error: " + e.getMessage());
        }
    }

    public static authData validUser(String authToken) throws DataAccessException {
        AuthDAO authDAO = MemoryAuthDAO.getInstance();
        return authDAO.getAuth(authToken);
    }
}
