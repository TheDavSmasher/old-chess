package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.SQLUserDAO;
import dataAccess.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SQLUserDAOTest {

    UserDAO userDAO;
    String username = "davhig22";
    String password = "pass123";
    String email = "davhig22@byu.edu";

    @BeforeEach
    void setUp() throws DataAccessException {
        userDAO = SQLUserDAO.getInstance();
        userDAO.clear();
    }
    @Test
    void getUserTest() {
    }

    @Test
    void getUserFail() {
    }

    @Test
    void createUserTest() {
    }

    @Test
    void createUserFail() {
    }

    @Test
    void clear() {
    }
}