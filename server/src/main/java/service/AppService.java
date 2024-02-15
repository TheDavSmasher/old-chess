package service;

import dataAccess.*;
import service.result.FailureResponse;
import service.result.Response;

public class AppService {
    public static Response clearData(){
        try {
            AuthDAO authDAO = MemoryAuthDAO.getInstance();
            UserDAO userDAO = MemoryUserDAO.getInstance();
            GameDAO gameDAO = MemoryGameDAO.getInstance();

            authDAO.clear();
            userDAO.clear();
            gameDAO.clear();

            return new Response(200);
        } catch (DataAccessException e) {
            return new FailureResponse(500, "Error: " + e.getMessage());
        }
    }
}
