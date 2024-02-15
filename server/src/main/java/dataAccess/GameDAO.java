package dataAccess;

import dataModels.gameData;
import java.util.ArrayList;

public interface GameDAO {
    ArrayList<gameData> listGames() throws DataAccessException;
    gameData getGame(int gameID) throws DataAccessException;
    gameData createGame(String gameName) throws DataAccessException;
    void updateGame(int gameID, String color, String username) throws DataAccessException;
    void deleteGame(int gameID) throws DataAccessException;
    void clear() throws DataAccessException;
}
