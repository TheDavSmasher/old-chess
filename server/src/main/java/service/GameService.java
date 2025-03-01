package service;

import dataAccess.GameDAO;
import model.dataAccess.AuthData;
import model.dataAccess.GameData;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.response.CreateGameResponse;
import model.response.EmptyResponse;
import model.response.ListGamesResponse;
import model.response.result.*;

public class GameService extends Service {
    public static ListGamesResponse getAllGames(String authToken) throws ServiceException {
        return tryCatch(() -> {
            if (UserService.getUser(authToken) == null) {
                throw new UnauthorizedException();
            }
            GameDAO gameDAO = GameDAO.getInstance();
            return new ListGamesResponse(gameDAO.listGames());
        });
    }

    public static CreateGameResponse createGame(CreateGameRequest request, String authToken) throws ServiceException {
        return tryCatch(() -> {
            if (UserService.getUser(authToken) == null) {
                throw new UnauthorizedException();
            }
            GameDAO gameDAO = GameDAO.getInstance();

            if (request.gameName() == null || request.gameName().isEmpty()) {
                throw new BadRequestException();
            }
            GameData newGame = gameDAO.createGame(request.gameName());
            return new CreateGameResponse(newGame.gameID());
        });
    }

    public static EmptyResponse joinGame(JoinGameRequest request, String authToken) throws ServiceException {
        return tryCatch(() -> {
            AuthData auth = UserService.getUser(authToken);
            if (auth == null) {
                throw new UnauthorizedException();
            }
            GameDAO gameDAO = GameDAO.getInstance();
            GameData oldGame = gameDAO.getGame(request.gameID());
            if (request.gameID() <= 0 || oldGame == null) {
                throw new BadRequestException();
            }
            if (request.playerColor() == null) {
                throw new BadRequestException();
            }
            String color = getColor(request.playerColor(), oldGame, auth.username());
            gameDAO.updateGamePlayer(request.gameID(), color, auth.username());
            return new EmptyResponse();
        });
    }

    public static GameData getGame(String authToken, int gameID) throws ServiceException {
        return tryCatch(() -> {
            AuthData auth = UserService.getUser(authToken);
            if (auth == null) {
                throw new UnauthorizedException();
            }
            GameDAO gameDAO = GameDAO.getInstance();
            return gameDAO.getGame(gameID);
        });
    }

    //WebSocket
    public static void leaveGame(String authToken, int gameID) throws ServiceException {
        tryCatch(() -> {
            AuthData auth = UserService.getUser(authToken);
            if (auth == null) {
                throw new UnauthorizedException();
            }
            GameDAO gameDAO = GameDAO.getInstance();
            GameData oldGame = gameDAO.getGame(gameID);
            if (gameID <= 0 || oldGame == null) {
                throw new BadRequestException();
            }
            if (oldGame.whiteUsername().equals(auth.username())) {
                gameDAO.updateGamePlayer(gameID, "WHITE", auth.username());
            } else if (oldGame.blackUsername().equals(auth.username())) {
                gameDAO.updateGamePlayer(gameID, "BLACK", auth.username());
            }
        });
    }

    public static void updateGameState(String authToken, int gameID, String gameJson) throws ServiceException {
        tryCatch(() -> {
            AuthData auth = UserService.getUser(authToken);
            if (auth == null) {
                throw new UnauthorizedException();
            }
            GameDAO gameDAO = GameDAO.getInstance();
            gameDAO.updateGameBoard(gameID, gameJson);
        });
    }

    private static String getColor(String playerColor, GameData oldGame, String username) throws ServiceException {
        String color = playerColor.toUpperCase();
        if (!color.equals("WHITE") && !color.equals("BLACK")) {
            throw new BadRequestException();
        }
        if (color.equals("WHITE") && oldGame.whiteUsername() != null && oldGame.whiteUsername().equals(username)
        || color.equals("BLACK") && oldGame.blackUsername() != null && oldGame.blackUsername().equals(username)) { //Rejoining game is allowed
            return color;
        }
        if (color.equals("WHITE") && oldGame.whiteUsername() != null    //Trying to take White, White player already taken
        || color.equals("BLACK") && oldGame.blackUsername() != null) {     //Trying to take Black, Black player already taken
            throw new PreexistingException();
        }
        return color;
    }
}
