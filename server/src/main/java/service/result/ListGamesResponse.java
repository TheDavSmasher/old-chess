package service.result;

import dataModels.gameData;

import java.util.ArrayList;

public record ListGamesResponse(ArrayList<gameData> games) {}
