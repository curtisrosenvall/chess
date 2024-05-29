package result;

import model.*;

import java.util.ArrayList;

public class ListGamesResult extends ParentResult {


    private ArrayList<GameData> gameList;

    public ListGamesResult(Boolean success, String message, ArrayList<GameData> gameList) {
        super(success, message);
        this.gameList = gameList;

    }

    public ArrayList<GameData> getGameList() {
        return gameList;
    }
}
