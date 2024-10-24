package result;

import models.GameData;

import java.util.ArrayList;

public class ListGamesRes extends ParentRes {

    private final ArrayList<GameData> games;

    public ListGamesRes(Boolean success, String message, ArrayList<GameData> games) {
        super(success, message);
        this.games = games;
    }

    public ArrayList<GameData> getGames() {
        return games;
    }
}