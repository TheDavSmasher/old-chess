package dataAccess;

import model.AuthData;

public class SQLAuthDAO implements AuthDAO {
    static SQLAuthDAO instance;

    public SQLAuthDAO() throws DataAccessException {
        DatabaseManager.configureDatabase();
    }
    @Override
    public AuthData getAuth(String token) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String token) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }

    static public AuthDAO getInstance() throws DataAccessException {
        if (instance == null) {
            instance = new SQLAuthDAO();
        }
        return instance;
    }
}
