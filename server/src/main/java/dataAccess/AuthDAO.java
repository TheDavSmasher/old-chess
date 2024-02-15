package dataAccess;

import dataModels.authData;

public interface AuthDAO {
    authData getAuth(String token) throws DataAccessException;
    authData createAuth(String username) throws DataAccessException;
    void deleteAuth(String token) throws DataAccessException;
    void clear() throws DataAccessException;
}
