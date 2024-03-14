package model.response;

import model.dataAccess.GameData;

import java.util.ArrayList;

public record ListGamesResponse(ArrayList<GameData> games) {}
