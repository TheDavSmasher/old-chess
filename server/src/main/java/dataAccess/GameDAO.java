package dataAccess;

import model.GameData;
import java.util.ArrayList;

public interface GameDAO {
    ArrayList<GameData> listGames() throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    GameData createGame(String gameName) throws DataAccessException;
    void updateGame(int gameID, String color, String username) throws DataAccessException;
    void deleteGame(int gameID) throws DataAccessException;
    void clear() throws DataAccessException;
}
