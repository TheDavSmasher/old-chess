package dataAccess;

import model.GameData;

import java.util.ArrayList;

public class SQLGameDAO implements GameDAO {
    static SQLGameDAO instance;

    public SQLGameDAO () throws DataAccessException {
        DatabaseManager.configureDatabase();
    }
    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        //FIXME SELECT gameID, whiteUsername, blackUsername, gameName FROM games
        return null;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        //FIXME SELECT * FROM games WHERE gameID = gameID
        return null;
    }

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        //FIXME INSERT INTO games (gameName, game) VALUES (gameName, newGame)
        return null;
    }

    @Override
    public void updateGame(int gameID, String color, String username) throws DataAccessException {
        //FIXME UPDATE games SET blackUsername/whiteUsername = username WHERE gameID = gameID
    }

    @Override
    public void deleteGame(int gameID) throws DataAccessException {
        //FIXME DELETE FROM games WHERE gameID = gameID
    }

    @Override
    public void clear() throws DataAccessException {
        //FIXME DELETE FROM games
    }

    static public GameDAO getInstance() throws DataAccessException {
        if (instance == null) {
            instance = new SQLGameDAO();
        }
        return instance;
    }
}
