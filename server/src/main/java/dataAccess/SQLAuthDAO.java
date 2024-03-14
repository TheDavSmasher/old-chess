package dataAccess;

import model.dataAccess.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO {
    static SQLAuthDAO instance;

    public SQLAuthDAO() throws DataAccessException {
        DatabaseManager.configureDatabase();
    }
    @Override
    public AuthData getAuth(String token) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM auth WHERE authToken=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, token);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (!rs.next()) return null;
                    String authToken = rs.getString("authToken");
                    String name = rs.getString("username");
                    return new AuthData(name, authToken);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
            String token = UUID.randomUUID().toString();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, token);
                preparedStatement.setString(2, username);

                if (preparedStatement.executeUpdate() == 0) {
                    throw new DataAccessException("Did not create any auth");
                }

                return new AuthData(username, token);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void deleteAuth(String token) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM auth WHERE authToken=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, token);

                if (preparedStatement.executeUpdate() == 0) {
                    throw new DataAccessException("Did not delete any auth");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("TRUNCATE auth")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    static public AuthDAO getInstance() throws DataAccessException {
        if (instance == null) {
            instance = new SQLAuthDAO();
        }
        return instance;
    }
}
