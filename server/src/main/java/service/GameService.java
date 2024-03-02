package service;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.SQLGameDAO;
import model.AuthData;
import model.GameData;
import service.request.AuthRequest;
import service.request.CreateGameRequest;
import service.request.JoinGameRequest;
import service.result.*;

public class GameService {
    public static ListGamesResponse getAllGames(AuthRequest auth) throws ServiceException {
        try {
            if (UserService.validUser(auth.authToken()) == null) {
                throw new UnauthorizedException();
            }
            GameDAO gameDAO = SQLGameDAO.getInstance();
            return new ListGamesResponse(gameDAO.listGames());
        } catch (DataAccessException e) {
            throw new UnexpectedException(e.getMessage());
        }
    }

    public static CreateGameResponse createGame(CreateGameRequest request, AuthRequest auth) throws ServiceException {
        try {
            if (UserService.validUser(auth.authToken()) == null) {
                throw new UnauthorizedException();
            }
            GameDAO gameDAO = SQLGameDAO.getInstance();

            if (request.gameName() == null || request.gameName().isEmpty()) {
                throw new BadRequestException();
            }
            GameData newGame = gameDAO.createGame(request.gameName());
            return new CreateGameResponse(newGame.gameID());
        } catch (DataAccessException e) {
            throw new UnexpectedException(e.getMessage());
        }
    }

    public static void joinGame(JoinGameRequest request, AuthRequest authRequest) throws ServiceException {
        try {
            AuthData auth = UserService.validUser(authRequest.authToken());
            if (auth == null) {
                throw new UnauthorizedException();
            }
            GameDAO gameDAO = SQLGameDAO.getInstance();
            GameData oldGame = gameDAO.getGame(request.gameID());
            if (request.gameID() <= 0 || oldGame == null) {
                throw new BadRequestException();
            }
            if (request.playerColor() != null) {
                if (!request.playerColor().equals("WHITE") && !request.playerColor().equals("BLACK")) {
                    throw new BadRequestException();
                }
                if (request.playerColor().equals("WHITE") && oldGame.whiteUsername() != null
                || request.playerColor().equals("BLACK") && oldGame.blackUsername() != null) {
                    throw new PreexistingException();
                }
                gameDAO.updateGame(request.gameID(), request.playerColor(), auth.username());
            }
        } catch (DataAccessException e) {
            throw new UnexpectedException(e.getMessage());
        }
    }
}
