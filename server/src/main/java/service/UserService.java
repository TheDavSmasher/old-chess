package service;

import dataAccess.*;
import model.AuthData;
import service.request.AuthRequest;
import service.request.UserEnterRequest;
import service.result.*;

public class UserService {
    public static UserEnterResponse register(UserEnterRequest request) throws ServiceException {
        try {
            UserDAO userDAO = MemoryUserDAO.getInstance();

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
            throw new UnexpectedException();
        }
    }

    public static UserEnterResponse login(UserEnterRequest request) throws ServiceException {
        try {
            UserDAO userDAO = MemoryUserDAO.getInstance();
            AuthDAO authDAO = MemoryAuthDAO.getInstance();

            if (userDAO.getUser(request.username(), request.password()) == null) {
                throw new UnauthorizedException();
            }
            AuthData newAuth = authDAO.createAuth(request.username());
            return new UserEnterResponse(newAuth.username(), newAuth.authToken());
        } catch (DataAccessException e) {
            throw new UnexpectedException();
        }
    }

    public static void logout(AuthRequest request) throws ServiceException {
        try {
            if (UserService.validUser(request.authToken()) == null) {
                throw new UnauthorizedException();
            }
            AuthDAO authDAO = MemoryAuthDAO.getInstance();
            authDAO.deleteAuth(request.authToken());
        } catch (DataAccessException e) {
            throw new UnexpectedException();
        }
    }

    public static AuthData validUser(String authToken) throws UnexpectedException {
        try {
            AuthDAO authDAO = MemoryAuthDAO.getInstance();
            return authDAO.getAuth(authToken);
        } catch (DataAccessException e) {
            throw new UnexpectedException();
        }
    }
}
