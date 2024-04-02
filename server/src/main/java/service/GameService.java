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
            if (UserService.getUser(auth.authToken()) == null) {
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
            if (UserService.getUser(auth.authToken()) == null) {
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
            AuthData auth = UserService.getUser(authRequest.authToken());
            if (auth == null) {
                throw new UnauthorizedException();
            }
            GameDAO gameDAO = SQLGameDAO.getInstance();
            GameData oldGame = gameDAO.getGame(request.gameID());
            if (request.gameID() <= 0 || oldGame == null) {
                throw new BadRequestException();
            }
            if (request.playerColor() != null) {
                String color = getColor(request.playerColor(), oldGame, auth.username());
                gameDAO.updateGamePlayer(request.gameID(), color, auth.username());
            }
        } catch (DataAccessException e) {
            throw new UnexpectedException(e.getMessage());
        }
    }

    public static GameData getGame(String authToken, int gameID) throws ServiceException {
        try {
            AuthData auth = UserService.getUser(authToken);
            if (auth == null) {
                throw new UnauthorizedException();
            }
            GameDAO gameDAO = SQLGameDAO.getInstance();
            return gameDAO.getGame(gameID);
        } catch (DataAccessException e) {
            throw new UnexpectedException(e.getMessage());
        }
    }

    public static void leaveGame(String authToken, int gameID) throws ServiceException {
        try {
            AuthData auth = UserService.getUser(authToken);
            if (auth == null) {
                throw new UnauthorizedException();
            }
            GameDAO gameDAO = SQLGameDAO.getInstance();
            GameData oldGame = gameDAO.getGame(gameID);
            if (gameID <= 0 || oldGame == null) {
                throw new BadRequestException();
            }
            if (oldGame.whiteUsername().equals(auth.username())) {
                gameDAO.updateGamePlayer(gameID, "WHITE", auth.username());
            } else if (oldGame.blackUsername().equals(auth.username())) {
                gameDAO.updateGamePlayer(gameID, "BLACK", auth.username());
            }
        } catch (DataAccessException e) {
            throw new UnexpectedException(e.getMessage());
        }
    }

    public static void updateGameState(String authToken, int gameID, String gameJson) throws ServiceException {
        try {
            AuthData auth = UserService.getUser(authToken);
            if (auth == null) {
                throw new UnauthorizedException();
            }
            GameDAO gameDAO = SQLGameDAO.getInstance();
            gameDAO.updateGameBoard(gameID, gameJson);
        } catch (DataAccessException e) {
            throw new UnexpectedException(e.getMessage());
        }
    }

    private static String getColor(String playerColor, GameData oldGame, String username) throws ServiceException {
        String color = playerColor.toUpperCase();
        if (!color.equals("WHITE") && !color.equals("BLACK")) {
            throw new BadRequestException();
        }
        if (color.equals("WHITE") && oldGame.whiteUsername() != null    //Trying to take White, White player already taken
        || color.equals("BLACK") && oldGame.blackUsername() != null     //Trying to take Black, Black player already taken
        || color.equals("WHITE") && oldGame.blackUsername().equals(username)     //Trying to play against self, client can only hold one player
        || color.equals("BLACK") && oldGame.whiteUsername().equals(username)) {  //Trying to play against self, client can only hold one player
            throw new PreexistingException();
        }
        return color;
    }
}
