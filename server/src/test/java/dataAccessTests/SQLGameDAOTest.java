package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.SQLGameDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SQLGameDAOTest {

    GameDAO gameDAO;
    String username = "davhig22";

    @BeforeEach
    void setUp() throws DataAccessException {
        gameDAO = SQLGameDAO.getInstance();
        gameDAO.clear();
    }
    @Test
    void listGamesTest() {
    }

    @Test
    void listGamesFail() {
    }

    @Test
    void getGameTest() {
    }

    @Test
    void getGameFail() {
    }

    @Test
    void createGameTest() {
    }

    @Test
    void createGameFail() {
    }

    @Test
    void updateGameTest() {
    }

    @Test
    void updateGameFail() {
    }

    @Test
    void clear() {
    }
}