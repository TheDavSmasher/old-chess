package dataAccess;

import model.UserData;

public class SQLUserDAO implements UserDAO {
    static SQLUserDAO instance;

    public SQLUserDAO () throws DataAccessException {
        DatabaseManager.configureDatabase();
    }
    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public UserData getUser(String username, String password) throws DataAccessException {
        return null;
    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }

    static public UserDAO getInstance() throws DataAccessException {
        if (instance == null) {
            instance = new SQLUserDAO();
        }
        return instance;
    }
}
