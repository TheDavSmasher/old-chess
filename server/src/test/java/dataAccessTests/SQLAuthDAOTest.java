package dataAccessTests;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SQLAuthDAOTest {

    AuthDAO authDAO;
    String username = "davhig22";

    @BeforeEach
    void setUp() throws DataAccessException {
        authDAO = SQLAuthDAO.getInstance();
        authDAO.clear();
    }

    @Test
    void getAuthTest() {
    }

    @Test
    void getAuthFail() {
    }

    @Test
    void createAuthTest() {
    }

    @Test
    void createAuthFail() {
    }

    @Test
    void deleteAuthTest() {
    }

    @Test
    void deleteAuthFail() {
    }

    @Test
    void clear() {
    }
}