package dataAccess;

import dataModels.gameData;
import java.util.ArrayList;

public interface GameDAO {
    ArrayList<gameData> listGames();
    gameData getGame(int gameID);
    int createGame(String gameName);
    void updateGame(int gameID, String color, String username) throws DataAccessException;
    void deleteGame(int gameID);
    void clear();
}
