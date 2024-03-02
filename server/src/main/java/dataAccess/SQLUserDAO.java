package dataAccess;

import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SQLUserDAO implements UserDAO {
    static SQLUserDAO instance;

    public SQLUserDAO () throws DataAccessException {
        DatabaseManager.configureDatabase();
    }
    @Override
    public UserData getUser(String username) throws DataAccessException {
        //FIXME SELECT FROM users WHERE username = username
        return null;
    }

    @Override
    public UserData getUser(String username, String password) throws DataAccessException {
        UserData userData = getUser(username);
        String storedPassword = userData.password();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(password);
        if (!encoder.matches(hashedPassword, storedPassword)) {
            return null;
        }
        return userData;
    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(password);

        //FIXME INSERT INTO users (username, password, email) VALUES (username, hashedPassword, email)
    }

    @Override
    public void clear() throws DataAccessException {
        //FIXME DELETE FROM users
    }

    static public UserDAO getInstance() throws DataAccessException {
        if (instance == null) {
            instance = new SQLUserDAO();
        }
        return instance;
    }
}
