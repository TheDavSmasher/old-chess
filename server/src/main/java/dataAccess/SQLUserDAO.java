package dataAccess;

import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {
    static SQLUserDAO instance;

    public SQLUserDAO () throws DataAccessException {
        DatabaseManager.configureDatabase();
    }
    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
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
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username, String password) throws DataAccessException {
        UserData userData = getUser(username);
        if (userData == null) return null;
        String storedPassword = userData.password();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(password, storedPassword)) return null;
        return userData;
    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(password);

        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, email);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "TRUNCATE users";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    static public UserDAO getInstance() throws DataAccessException {
        if (instance == null) {
            instance = new SQLUserDAO();
        }
        return instance;
    }
}
