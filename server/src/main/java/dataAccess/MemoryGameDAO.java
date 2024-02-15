package dataAccess;

import chess.ChessGame;
import dataModels.gameData;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO {
    static MemoryGameDAO instance;
    private final HashSet<gameData> data;

    MemoryGameDAO() {
        data = new HashSet<>();
    }
    @Override
    public ArrayList<gameData> listGames() {
        return new ArrayList<gameData>(data);
    }

    @Override
    public gameData getGame(int gameID) {
        for (gameData game : data) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        return null;
    }

    @Override
    public int createGame(String gameName) {
        int newID = data.size() + 1;
        data.add(new gameData(newID, null, null, gameName, new ChessGame()));
        return newID;
    }

    @Override
    public void updateGame(int gameID, String color, String username) {
        gameData oldGame = getGame(gameID);
        if (Objects.equals(color, "WHITE")) {
            data.add(new gameData(oldGame.gameID(), username, oldGame.blackUsername(), oldGame.gameName(), oldGame.game()));
        } else {
            data.add(new gameData(oldGame.gameID(), oldGame.whiteUsername(), username, oldGame.gameName(), oldGame.game()));
        }
        data.remove(oldGame);
    }

    @Override
    public void deleteGame(int gameID) {
        data.removeIf(game -> game.gameID() == gameID);
    }

    @Override
    public void clear() {
        data.clear();
    }

    static GameDAO getInstance() {
        if (instance == null) {
            instance = new MemoryGameDAO();
        }
        return instance;
    }
}
