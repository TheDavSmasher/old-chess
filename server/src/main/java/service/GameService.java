package service;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.MemoryGameDAO;
import dataModels.authData;
import dataModels.gameData;
import service.request.AuthRequest;
import service.request.CreateGameRequest;
import service.request.JoinGameRequest;
import service.result.CreateGameResponse;
import service.result.FailureResponse;
import service.result.ListGamesResponse;
import service.result.Response;

import java.util.ArrayList;

public class GameService {
    public static Response getAllGames(AuthRequest request) {
        try {
            GameDAO gameDAO = MemoryGameDAO.getInstance();

            if (UserService.validUser(request.authToken()) == null) {
                return new FailureResponse(401,"Error: unauthorized");
            }
            return new ListGamesResponse(200, gameDAO.listGames());
        } catch (DataAccessException e) {
            return new FailureResponse(500, "Error: " + e.getMessage());
        }
    }

    public static Response createGame(CreateGameRequest request) {
        try {
            GameDAO gameDAO = MemoryGameDAO.getInstance();

            if (request.gameName() == null || request.gameName().isEmpty()) {
                return new FailureResponse(400,"Error: bad request");
            }
            if (UserService.validUser(request.authToken()) == null) {
                return new FailureResponse(401,"Error: unauthorized");
            }
            gameData newGame = gameDAO.createGame(request.gameName());
            return new CreateGameResponse(200, newGame.gameID());
        } catch (DataAccessException e) {
            return new FailureResponse(500, "Error: " + e.getMessage());
        }
    }

    public static Response joinGame(JoinGameRequest request) {
        try {
            GameDAO gameDAO = MemoryGameDAO.getInstance();

            authData auth = UserService.validUser(request.authToken());
            if (auth == null) {
                return new FailureResponse(401,"Error: unauthorized");
            }
            if (request.gameID() <= 0 || gameDAO.getGame(request.gameID()) == null) {
                return new FailureResponse(400,"Error: bad request");
            }
            if (request.playerColor() != null) {
                if (!request.playerColor().equals("WHITE") && !request.playerColor().equals("BLACK")) {
                    return new FailureResponse(400,"Error: bad request");
                }
                if (request.playerColor().equals("WHITE") && gameDAO.getGame(request.gameID()).whiteUsername() != null
                || request.playerColor().equals("BLACK") && gameDAO.getGame(request.gameID()).blackUsername() != null) {
                    return new FailureResponse(403,"Error: already taken");
                }
                gameDAO.updateGame(request.gameID(), request.playerColor(), auth.username());
            }
            return new Response(200);
        } catch (DataAccessException e) {
            return new FailureResponse(500, "Error: " + e.getMessage());
        }
    }
}
