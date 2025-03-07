package dataAccess;

import model.dataAccess.UserData;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SQLUserDAO extends SQLDAO implements UserDAO {
    private static SQLUserDAO instance;

    public SQLUserDAO () throws DataAccessException {
        configureDatabase();
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return tryStatement(connection -> {
            String sql = "SELECT * FROM users WHERE username =?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (!rs.next()) return null;
                    String name = rs.getString("username");
                    String password = rs.getString("password");
                    String email = rs.getString("email");
                    return new UserData(name, password, email);
                }
            }
        });
    }

    @Override
    public UserData getUser(String username, String password) throws DataAccessException {
        UserData userData = getUser(username);
        if (userData == null) return null;
        String storedPassword = userData.password();

        if (!BCrypt.checkpw(password, storedPassword)) return null;
        return userData;
    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        tryStatement(connection -> {
            String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, email);

                if (preparedStatement.executeUpdate() == 0) {
                    throw new DataAccessException("Did not create any user");
                }
            }
        });
    }

    @Override
    public void clear() throws DataAccessException {
        tryStatement(connection -> {
            String sql = "TRUNCATE users";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.executeUpdate();
            }
        });
    }

    public static UserDAO getInstance() throws DataAccessException {
        if (instance == null) {
            instance = new SQLUserDAO();
        }
        return instance;
    }
}
