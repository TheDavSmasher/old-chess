package dataAccess;

import model.AuthData;

public class SQLAuthDAO implements AuthDAO {
    static SQLAuthDAO instance;

    public SQLAuthDAO() throws DataAccessException {
        DatabaseManager.configureDatabase();
    }
    @Override
    public AuthData getAuth(String token) throws DataAccessException {
        //FIXME SELECT * FROM auth WHERE authToken = token
        return null;
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        //FIXME INSERT INTO auth (authToken, username) VALUES (newToken, username)
        return null;
    }

    @Override
    public void deleteAuth(String token) throws DataAccessException {
        //FIXME DELETE FROM auth WHERE authToken = token
    }

    @Override
    public void clear() throws DataAccessException {
        //FIXME DELETE FROM auth
    }

    static public AuthDAO getInstance() throws DataAccessException {
        if (instance == null) {
            instance = new SQLAuthDAO();
        }
        return instance;
    }
}
