package dataAccess;

import dataAccess.memory.MemoryAuthDAO;
import dataAccess.sql.SQLAuthDAO;
import model.dataAccess.AuthData;
import service.Service;

public interface AuthDAO {
    AuthData getAuth(String token) throws DataAccessException;
    AuthData createAuth(String username) throws DataAccessException;
    void deleteAuth(String token) throws DataAccessException;
    void clear() throws DataAccessException;
    static AuthDAO getInstance() throws DataAccessException {
        if (Service.UseSQL) {
            return SQLAuthDAO.getInstance();
        }
        return MemoryAuthDAO.getInstance();
    }
}
