package dataaccess;
import java.util.ArrayList;
import chess.ChessGame;
import model.GameData;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {

    HashMap<Integer, GameData> mapOfGameData;
    int numberOfGames;

    public MemoryGameDAO() {
        mapOfGameData = new HashMap<>();
        numberOfGames = 0;
    }


    @Override
    public void clear(){
        mapOfGameData.clear();
    }

    @Override
    public void createGame(String name) {
        numberOfGames++;
        mapOfGameData.put(numberOfGames, new GameData(numberOfGames, null, null, name, new ChessGame()));
    }

    @Override
    public GameData getGame(int id) {
        return mapOfGameData.get(id);
    }

    @Override
    public ArrayList<GameData> getAllGames() {
        ArrayList<GameData> listOfGames = new ArrayList<>();
        for(int i = 1; i <= numberOfGames; i++) {
            listOfGames.add(getGame(i));
        }
        return listOfGames;
    }

    @Override
    public void updateGame(int id, GameData game) {
        mapOfGameData.put(id, game);
    }


    @Override
    public int size() {
        return mapOfGameData.size();
    }


}
