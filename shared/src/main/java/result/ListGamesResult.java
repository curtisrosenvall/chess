package result;

import model.GameData;

import java.util.ArrayList;

public class ListGamesResult extends ParentResult {

    private final ArrayList<GameData> games;

    public ListGamesResult(Boolean success, String message, ArrayList<GameData> games) {
        super(success, message);
        this.games = games;
    }

    public ArrayList<GameData> getGames() {
        return games;
    }
}