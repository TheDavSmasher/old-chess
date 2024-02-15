package service;

import dataAccess.*;
import dataModels.authData;
import dataModels.userData;
import service.request.AuthRequest;
import service.result.FailureResponse;
import service.result.Response;
import service.result.UserEnterResponse;

public class UserService {
    public static Response register(userData request) {
        try {
            UserDAO userDAO = MemoryUserDAO.getInstance();

            if (request.username().isEmpty() || request.password().isEmpty() || request.email().isEmpty()) {
                return new FailureResponse(400,"Error: bad request");
            }
            if (userDAO.getUser(request.username()) != null) {
                return new FailureResponse(403,"Error: already taken");
            }
            userDAO.createUser(request.username(), request.password(), request.email());
            return UserService.login(request);
        } catch (DataAccessException e) {
            return new FailureResponse(500, "Error: " + e.getMessage());
        }
    }

    public static Response login(userData request) {
        try {
            UserDAO userDAO = MemoryUserDAO.getInstance();
            AuthDAO authDAO = MemoryAuthDAO.getInstance();

            if (userDAO.getUser(request.username()) == null) {
                return new FailureResponse(401,"Error: unauthorized");
            }
            authData newAuth = authDAO.createAuth(request.username());
            return new UserEnterResponse(200, newAuth.username(), newAuth.authToken());
        } catch (DataAccessException e) {
            return new FailureResponse(500, "Error: " + e.getMessage());
        }
    }

    public static Response logout(AuthRequest request) {
        try {
            AuthDAO authDAO = MemoryAuthDAO.getInstance();

            if (UserService.validUser(request.authToken()) == null) {
                return new FailureResponse(401,"Error: unauthorized");
            }
            authDAO.deleteAuth(request.authToken());
            return new Response(200);
        } catch (DataAccessException e) {
            return new FailureResponse(500, "Error: " + e.getMessage());
        }
    }

    public static authData validUser(String authToken) throws DataAccessException {
        AuthDAO authDAO = MemoryAuthDAO.getInstance();
        return authDAO.getAuth(authToken);
    }
}
