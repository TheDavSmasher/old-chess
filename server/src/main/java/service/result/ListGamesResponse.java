package service.result;

import dataModels.gameData;

import java.util.ArrayList;

public class ListGamesResponse extends Response {
    private final ArrayList<gameData> games;

    public ListGamesResponse(int status, ArrayList<gameData> games) {
        super(status);
        this.games = games;
    }
}
