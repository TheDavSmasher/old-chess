package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.dataAccess.GameData;

import java.sql.*;
import java.util.ArrayList;

public class SQLGameDAO implements GameDAO {
    static SQLGameDAO instance;

    public SQLGameDAO () throws DataAccessException {
        DatabaseManager.configureDatabase();
    }
    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        ArrayList<GameData> gameList = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "SELECT gameID, whiteUsername, blackUsername, gameName FROM games";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("gameID");
                        String white = rs.getString("whiteUsername");
                        String black = rs.getString("blackUsername");
                        String name = rs.getString("gameName");

                        gameList.add(new GameData(id, white, black, name, null));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return gameList;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM games WHERE gameID =?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, gameID);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (!rs.next()) return null;
                    int id = rs.getInt("gameID");
                    String white = rs.getString("whiteUsername");
                    String black = rs.getString("blackUsername");
                    String name = rs.getString("gameName");
                    String gameJson = rs.getString("game");
                    ChessGame game = new Gson().fromJson(gameJson, ChessGame.class);

                    return new GameData(id, white, black, name, game);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO games (gameName, game) VALUES (?, ?)";
            ChessGame game = new ChessGame();
            String gameJson = new Gson().toJson(game);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, gameName);
                preparedStatement.setString(2, gameJson);

                if (preparedStatement.executeUpdate() == 0) {
                    throw new DataAccessException("Did not create any game");
                }

                int id;
                try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                    rs.next();
                    id = rs.getInt(1);
                }

                return new GameData(id, null, null, gameName, game);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void updateGamePlayer(int gameID, String color, String username) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql;
            if (color.equals("WHITE")) sql = "UPDATE games SET whiteUsername=? WHERE gameID=?";
            else sql = "UPDATE games SET blackUsername=? WHERE gameID=?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                preparedStatement.setInt(2, gameID);

                if (preparedStatement.executeUpdate() == 0) {
                    throw new DataAccessException("Did not update any game");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void updateGameBoard(int gameID, String gameJson) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "UPDATE games SET game=? WHERE gameID=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, gameJson);
                preparedStatement.setInt(2, gameID);

                if (preparedStatement.executeUpdate() == 0) {
                    throw new DataAccessException("Did not update any game");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "TRUNCATE games";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    static public GameDAO getInstance() throws DataAccessException {
        if (instance == null) {
            instance = new SQLGameDAO();
        }
        return instance;
    }
}
