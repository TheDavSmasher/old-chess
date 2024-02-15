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
import service.result.ListGamesResponse;
import service.result.ServiceException;

public class GameService {
    public static ListGamesResponse getAllGames(AuthRequest request) throws ServiceException {
        try {
            GameDAO gameDAO = MemoryGameDAO.getInstance();

            if (UserService.validUser(request.authToken()) == null) {
                throw new ServiceException("Error: unauthorized");
            }
            return new ListGamesResponse(gameDAO.listGames());
        } catch (DataAccessException e) {
            throw new ServiceException("Error: " + e.getMessage());
        }
    }

    public static CreateGameResponse createGame(CreateGameRequest request) throws ServiceException {
        try {
            GameDAO gameDAO = MemoryGameDAO.getInstance();

            if (request.gameName() == null || request.gameName().isEmpty()) {
                throw new ServiceException("Error: bad request");
            }
            if (UserService.validUser(request.authToken()) == null) {
                throw new ServiceException("Error: unauthorized");
            }
            gameData newGame = gameDAO.createGame(request.gameName());
            return new CreateGameResponse(newGame.gameID());
        } catch (DataAccessException e) {
            throw new ServiceException("Error: " + e.getMessage());
        }
    }

    public static void joinGame(JoinGameRequest request) throws ServiceException {
        try {
            GameDAO gameDAO = MemoryGameDAO.getInstance();

            authData auth = UserService.validUser(request.authToken());
            if (auth == null) {
                throw new ServiceException("Error: unauthorized");
            }
            if (request.gameID() <= 0 || gameDAO.getGame(request.gameID()) == null) {
                throw new ServiceException("Error: bad request");
            }
            if (request.playerColor() != null) {
                if (!request.playerColor().equals("WHITE") && !request.playerColor().equals("BLACK")) {
                    throw new ServiceException("Error: bad request");
                }
                if (request.playerColor().equals("WHITE") && gameDAO.getGame(request.gameID()).whiteUsername() != null
                || request.playerColor().equals("BLACK") && gameDAO.getGame(request.gameID()).blackUsername() != null) {
                    throw new ServiceException("Error: already taken");
                }
                gameDAO.updateGame(request.gameID(), request.playerColor(), auth.username());
            }
        } catch (DataAccessException e) {
            throw new ServiceException("Error: " + e.getMessage());
        }
    }
}
