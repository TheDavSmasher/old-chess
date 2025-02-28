package dataAccess;

import model.dataAccess.AuthData;

public interface AuthDAO {
    AuthData getAuth(String token) throws DataAccessException;
    AuthData createAuth(String username) throws DataAccessException;
    void deleteAuth(String token) throws DataAccessException;
    void clear() throws DataAccessException;
    static AuthDAO getInstance(boolean useSQL) throws DataAccessException {
        if (useSQL) {
            return SQLAuthDAO.getInstance();
        }
        return MemoryAuthDAO.getInstance();
    }
}
