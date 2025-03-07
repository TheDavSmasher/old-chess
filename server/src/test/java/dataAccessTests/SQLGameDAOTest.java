package dataAccessTests;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.sql.SQLGameDAO;
import model.dataAccess.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SQLGameDAOTest {

    GameDAO gameDAO;
    String username = "davhig22";
    String gameName = "gameName";

    @BeforeEach
    void setUp() throws DataAccessException {
        gameDAO = SQLGameDAO.getInstance();
        gameDAO.clear();
    }
    @Test
    void listGamesTest() throws DataAccessException {
        gameDAO.createGame(gameName);
        Assertions.assertEquals(1, gameDAO.listGames().size());
    }

    @Test
    void listGamesFail() throws DataAccessException {
        Assertions.assertNotNull(gameDAO.listGames());
        Assertions.assertTrue(gameDAO.listGames().isEmpty());
    }

    @Test
    void getGameTest() throws DataAccessException {
        gameDAO.createGame(gameName);
        gameDAO.createGame(gameName);

        Assertions.assertEquals(new GameData(1, gameName, new ChessGame()), gameDAO.getGame(1));
        Assertions.assertEquals(new GameData(2, gameName, new ChessGame()), gameDAO.getGame(2));
    }

    @Test
    void getGameFail() throws DataAccessException {
        Assertions.assertNull(gameDAO.getGame(1));
        gameDAO.createGame(gameName);
        Assertions.assertNull(gameDAO.getGame(0));
    }

    @Test
    void createGameTest() throws DataAccessException {
        Assertions.assertEquals(new GameData(1, gameName, new ChessGame()), gameDAO.createGame(gameName));
        Assertions.assertEquals(new GameData(2, gameName, new ChessGame()), gameDAO.createGame(gameName));
    }

    @Test
    void createGameFail() {
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.createGame(null));
    }

    @Test
    void updateGamePlayerTest() throws DataAccessException {
        gameDAO.createGame(gameName);

        Assertions.assertDoesNotThrow(() -> gameDAO.updateGamePlayer(1, "WHITE", username));
        Assertions.assertDoesNotThrow(() -> gameDAO.updateGamePlayer(1, "BLACK", username));
    }

    @Test
    void updateGamePlayerFail() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.updateGamePlayer(1, "WHITE", username));
        gameDAO.createGame(gameName);
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.updateGamePlayer(0, "WHITE", username));
    }

    @Test
    void updateGameBoardTest() throws DataAccessException, InvalidMoveException {
        gameDAO.createGame(gameName);
        ChessGame game = gameDAO.getGame(1).game();
        Assertions.assertDoesNotThrow(() -> gameDAO.updateGameBoard(1, new Gson().toJson(game)));

        game.makeMove(new ChessMove(new ChessPosition(2,2), new ChessPosition(3,2),null));

        Assertions.assertDoesNotThrow(() -> gameDAO.updateGameBoard(1, new Gson().toJson(game)));
        Assertions.assertEquals(game, gameDAO.getGame(1).game());
    }

    @Test
    void updateGameBoardFail() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.updateGameBoard(1, "gameString"));
        gameDAO.createGame(gameName);
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.updateGameBoard(0, "gameString"));
        gameDAO.updateGameBoard(1, "gameString");
        Assertions.assertThrows(JsonSyntaxException.class, () -> gameDAO.getGame(1));
    }

    @Test
    void clear() throws DataAccessException {
        gameDAO.createGame(gameName);

        Assertions.assertDoesNotThrow(() -> gameDAO.clear());
        Assertions.assertDoesNotThrow(() -> gameDAO.clear()); // Multiple clears
    }
}