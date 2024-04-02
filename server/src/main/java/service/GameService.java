package service;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.SQLGameDAO;
import model.dataAccess.AuthData;
import model.dataAccess.GameData;
import model.request.AuthRequest;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.response.CreateGameResponse;
import model.response.ListGamesResponse;
import model.response.result.*;

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
                String color = getColor(request, oldGame, auth);
                gameDAO.updateGamePlayer(request.gameID(), color, auth.username());
            }
        } catch (DataAccessException e) {
            throw new UnexpectedException(e.getMessage());
        }
    }

    private static String getColor(JoinGameRequest request, GameData oldGame, AuthData auth) throws ServiceException {
        String color = request.playerColor().toUpperCase();
        if (!color.equals("WHITE") && !color.equals("BLACK")) {
            throw new BadRequestException();
        }
        if (color.equals("WHITE") && oldGame.whiteUsername() != null    //Trying to take White, White player already taken
        || color.equals("BLACK") && oldGame.blackUsername() != null     //Trying to take Black, Black player already taken
        || color.equals("WHITE") && oldGame.blackUsername().equals(auth.username())     //Trying to play against self, client can only hold one player
        || color.equals("BLACK") && oldGame.whiteUsername().equals(auth.username())) {  //Trying to play against self, client can only hold one player
            throw new PreexistingException();
        }
        return color;
    }
}
