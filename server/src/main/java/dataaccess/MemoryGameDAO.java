package dataaccess;

import chess.ChessGame;
import model.GameData;
import java.util.ArrayList;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{

    HashMap<Integer, GameData> map;
    int numGames;

    public MemoryGameDAO() {
        map = new HashMap<>();
        numGames = 0;
    }

    @Override
    public void clear() {
        map.clear();
        numGames = 0;
    }

    @Override
    public void createGame(String name) {
        numGames++;
        map.put(numGames, new GameData(numGames, null, null, name, new ChessGame()));
    }

    @Override
    public GameData getGame(int id) {
        return map.get(id);
    }

    @Override
    public ArrayList<GameData> listGames() {
        ArrayList<GameData> gameList = new ArrayList<>();
        for(int i = 1; i <= numGames; i++) {
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
