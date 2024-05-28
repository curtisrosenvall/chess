package response;

import model.*;

import java.util.ArrayList;

public class ListGamesResponse extends ParentResponse {


    private ArrayList<GameData> gameList;

    public ListGamesResponse(Boolean success, String message, ArrayList<GameData> gameList) {
        super(success, message);
        this.gameList = gameList;

    }

    public ArrayList<GameData> getGameList() {
        return gameList;
    }
}
