package dataAccess;

import model.AuthData;

public class SQLAuthDao implements AuthDAO {
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
}
