package dataaccess;

import models.GameData;
import chess.ChessGame;
import java.util.ArrayList;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    HashMap<Integer, GameData> map;
    int numberOfGames;

    public MemoryGameDAO() {
        map = new HashMap<>();
        numberOfGames = 0;
    }

    @Override
    public void clear() {
        map.clear();
        numberOfGames = 0;
    }

    @Override
    public void createGame(String name) {
        numberOfGames++;
        map.put(numberOfGames, new GameData(numberOfGames, null, null, name, new ChessGame()));
    }

    @Override
    public GameData getGame(int id) {
        return map.get(id);
    }

    @Override
    public ArrayList<GameData> listGames() {
        ArrayList<GameData> gameList = new ArrayList<>();
        for(int i = 1; i <= numberOfGames; i++) {
            gameList.add(getGame(i));
        }
        return gameList;
    }

    @Override
    public void updateGame(int id, GameData game) {
        map.put(id, game);
    }

    @Override
    public int size() {
        return map.size();
    }
}
