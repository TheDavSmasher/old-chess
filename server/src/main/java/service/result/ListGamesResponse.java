package service.result;

import dataModels.GameData;

import java.util.ArrayList;

public record ListGamesResponse(ArrayList<GameData> games) {}
