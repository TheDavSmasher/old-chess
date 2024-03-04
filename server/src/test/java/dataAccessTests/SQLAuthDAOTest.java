package dataAccessTests;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
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
    void getAuthTest() throws DataAccessException {
        String token = authDAO.createAuth(username).authToken();

        Assertions.assertEquals(new AuthData(username, token), authDAO.getAuth(token));
    }

    @Test
    void getAuthFail() throws DataAccessException {
        Assertions.assertNull(authDAO.getAuth(null));
        Assertions.assertNull(authDAO.getAuth("not-an-auth-token"));
    }

    @Test
    void createAuthTest() throws DataAccessException {
        AuthData authData = authDAO.createAuth(username);
        Assertions.assertEquals(username, authData.username());
        Assertions.assertNotNull(authData.authToken());
    }

    @Test
    void createAuthFail() {
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.createAuth(null));
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