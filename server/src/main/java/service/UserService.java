package service;

import dataAccess.*;
import model.dataAccess.AuthData;
import model.request.UserEnterRequest;
import model.response.EmptyResponse;
import model.response.UserEnterResponse;
import model.response.result.*;

public class UserService extends Service {
    public static UserEnterResponse register(UserEnterRequest request) throws ServiceException {
        return tryCatch(() -> {
            UserDAO userDAO = UserDAO.getInstance();

            if (request.username() == null || request.password() == null || request.email() == null ||
                    request.username().isEmpty() || request.password().isEmpty() || request.email().isEmpty()) {
                throw new BadRequestException();
            }
            if (userDAO.getUser(request.username()) != null) {
                throw new PreexistingException();
            }
            userDAO.createUser(request.username(), request.password(), request.email());
            return UserService.login(request);
        });
    }

    public static UserEnterResponse login(UserEnterRequest request) throws ServiceException {
        return tryCatch(() -> {
            UserDAO userDAO = UserDAO.getInstance();
            AuthDAO authDAO = AuthDAO.getInstance();

            if (userDAO.getUser(request.username(), request.password()) == null) {
                throw new UnauthorizedException();
            }
            AuthData newAuth = authDAO.createAuth(request.username());
            return new UserEnterResponse(newAuth.username(), newAuth.authToken());
        });
    }

    public static EmptyResponse logout(String authToken) throws ServiceException {
        return tryCatch(() -> {
            if (UserService.getUser(authToken) == null) {
                throw new UnauthorizedException();
            }
            AuthDAO authDAO = AuthDAO.getInstance();
            authDAO.deleteAuth(authToken);
            return new EmptyResponse();
        });
    }

    public static AuthData getUser(String authToken) throws ServiceException {
        return tryCatch(() -> {
            AuthDAO authDAO = AuthDAO.getInstance();
            return authDAO.getAuth(authToken);
        });
    }
}
