package dataAccess.sql;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.dataAccess.AuthData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class SQLAuthDAO extends SQLDAO implements AuthDAO {
    private static SQLAuthDAO instance;

    public SQLAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public AuthData getAuth(String token) throws DataAccessException {
        return tryStatement(connection -> {
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
        });
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        return tryStatement(connection -> {
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
        });
    }

    @Override
    public void deleteAuth(String token) throws DataAccessException {
        tryStatement(connection -> {
            String sql = "DELETE FROM auth WHERE authToken=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, token);

                if (preparedStatement.executeUpdate() == 0) {
                    throw new DataAccessException("Did not delete any auth");
                }
            }
        });
    }

    @Override
    public void clear() throws DataAccessException {
        tryStatement(connection -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement("TRUNCATE auth")) {
                preparedStatement.executeUpdate();
            }
        });
    }

    public static AuthDAO getInstance() throws DataAccessException {
        if (instance == null) {
            instance = new SQLAuthDAO();
        }
        return instance;
    }
}
