package service;

import dataAccess.*;
import model.dataAccess.AuthData;
import model.request.UserEnterRequest;
import model.response.UserEnterResponse;
import model.response.result.*;

public class UserService {
    public static UserEnterResponse register(UserEnterRequest request) throws ServiceException {
        try {
            UserDAO userDAO = SQLUserDAO.getInstance();

            if (request.username() == null || request.password() == null || request.email() == null ||
                    request.username().isEmpty() || request.password().isEmpty() || request.email().isEmpty()) {
                throw new BadRequestException();
            }
            if (userDAO.getUser(request.username()) != null) {
                throw new PreexistingException();
            }
            userDAO.createUser(request.username(), request.password(), request.email());
            return UserService.login(request);
        } catch (DataAccessException e) {
            throw new UnexpectedException(e.getMessage());
        }
    }

    public static UserEnterResponse login(UserEnterRequest request) throws ServiceException {
        try {
            UserDAO userDAO = SQLUserDAO.getInstance();
            AuthDAO authDAO = SQLAuthDAO.getInstance();

            if (userDAO.getUser(request.username(), request.password()) == null) {
                throw new UnauthorizedException();
            }
            AuthData newAuth = authDAO.createAuth(request.username());
            return new UserEnterResponse(newAuth.username(), newAuth.authToken());
        } catch (DataAccessException e) {
            throw new UnexpectedException(e.getMessage());
        }
    }

    public static void logout(String authToken) throws ServiceException {
        try {
            if (UserService.getUser(authToken) == null) {
                throw new UnauthorizedException();
            }
            AuthDAO authDAO = SQLAuthDAO.getInstance();
            authDAO.deleteAuth(authToken);
        } catch (DataAccessException e) {
            throw new UnexpectedException(e.getMessage());
        }
    }

    public static AuthData getUser(String authToken) throws UnexpectedException {
        try {
            AuthDAO authDAO = SQLAuthDAO.getInstance();
            return authDAO.getAuth(authToken);
        } catch (DataAccessException e) {
            throw new UnexpectedException(e.getMessage());
        }
    }
}
