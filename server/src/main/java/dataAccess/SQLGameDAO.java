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
        return null;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(int gameID, String color, String username) throws DataAccessException {

    }

    @Override
    public void deleteGame(int gameID) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }

    static public GameDAO getInstance() throws DataAccessException {
        if (instance == null) {
            instance = new SQLGameDAO();
        }
        return instance;
    }
}
