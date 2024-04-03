package serviceTests;

import model.dataAccess.GameData;
import model.response.CreateGameResponse;
import model.response.ListGamesResponse;
import model.response.result.BadRequestException;
import model.response.result.ServiceException;
import model.response.result.UnauthorizedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.AppService;
import service.GameService;
import service.UserService;
import model.request.AuthRequest;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.UserEnterRequest;

import java.util.ArrayList;

class GameServiceTest {

    String authToken;
    AuthRequest authRequest;
    String wrongAuthToken = "not-an-auth-token";
    AuthRequest wrongAuthRequest;

    @BeforeEach
    public void setUp() throws ServiceException {
        AppService.clearData();
        authToken = UserService.register(new UserEnterRequest("davhig22", "pass123", "davhig22@byu.edu")).authToken();
        authRequest = new AuthRequest(authToken);
    }

    @Test
    public void listGamesTest() throws ServiceException {
        ListGamesResponse expected = new ListGamesResponse(new ArrayList<>());
        Assertions.assertEquals(expected, GameService.getAllGames(authRequest));

        ArrayList<GameData> gamesList = new ArrayList<>();
        gamesList.add(new GameData(1, null, null, "game_1", null));
        expected = new ListGamesResponse(gamesList);
        GameService.createGame(new CreateGameRequest("game_1"), authRequest);
        Assertions.assertEquals(expected, GameService.getAllGames(authRequest));

        gamesList.add(new GameData(2, null, null, "game_2", null));
        gamesList.add(new GameData(3, null, null, "game_3", null));
        expected = new ListGamesResponse(gamesList);
        GameService.createGame(new CreateGameRequest("game_2"), authRequest);
        GameService.createGame(new CreateGameRequest("game_3"), authRequest);
        //FIXME test without ordering being an issue
        Assertions.assertEquals(expected, GameService.getAllGames(authRequest));
    }

    @Test
    public void listGamesFail() throws ServiceException {
        wrongAuthRequest = new AuthRequest(wrongAuthToken);
        Assertions.assertThrows(UnauthorizedException.class, () -> GameService.getAllGames(wrongAuthRequest));
    }

    @Test
    public void createGameTest() throws ServiceException {
        CreateGameRequest createGameRequest = new CreateGameRequest("gameName");
        Assertions.assertEquals(new CreateGameResponse(1), GameService.createGame(createGameRequest, authRequest));

        createGameRequest = new CreateGameRequest("gameName");
        Assertions.assertEquals(new CreateGameResponse(2), GameService.createGame(createGameRequest, authRequest));
    }

    @Test
    public void createGameFail() throws ServiceException {
        CreateGameRequest createGameRequest = new CreateGameRequest("gameName");

        wrongAuthRequest = new AuthRequest(wrongAuthToken);
        Assertions.assertThrows(UnauthorizedException.class, () -> GameService.createGame(createGameRequest, wrongAuthRequest));

        CreateGameRequest nullRequest = new CreateGameRequest(null);
        Assertions.assertThrows(BadRequestException.class, () -> GameService.createGame(nullRequest, authRequest));

        CreateGameRequest badRequest = new CreateGameRequest("");
        Assertions.assertThrows(BadRequestException.class, () -> GameService.createGame(badRequest, authRequest));
    }

    @Test
    public void joinGameTest() throws ServiceException {
        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", 1);
        JoinGameRequest observerRequest = new JoinGameRequest(null, 1);

        GameService.createGame(new CreateGameRequest("gameName"), authRequest);

        Assertions.assertDoesNotThrow(() -> GameService.joinGame(joinGameRequest, authRequest));
        Assertions.assertDoesNotThrow(() -> GameService.joinGame(observerRequest, authRequest));
    }

    @Test
    public void joinGameFail() throws ServiceException {
        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", 1);

        GameService.createGame(new CreateGameRequest("gameName"), authRequest);
        wrongAuthRequest = new AuthRequest(wrongAuthToken);
        Assertions.assertThrows(UnauthorizedException.class, () -> GameService.joinGame(joinGameRequest, wrongAuthRequest));

        JoinGameRequest badColorRequest = new JoinGameRequest("yellow", 1);
        Assertions.assertThrows(BadRequestException.class, () -> GameService.joinGame(badColorRequest, authRequest));

        JoinGameRequest nullGameRequest = new JoinGameRequest("WHITE", 2);
        Assertions.assertThrows(BadRequestException.class, () -> GameService.joinGame(nullGameRequest, authRequest));
    }
}