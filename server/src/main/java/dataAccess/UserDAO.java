package dataAccess;

import dataModels.userData;

public interface UserDAO {
    userData getUser(String username) throws DataAccessException;
    userData getUser(String username, String password) throws DataAccessException;
    void createUser(String username, String password, String email) throws DataAccessException;
    void deleteUser(String username) throws DataAccessException;
    void clear() throws DataAccessException;
}
