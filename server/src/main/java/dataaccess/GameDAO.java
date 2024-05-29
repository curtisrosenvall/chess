package dataaccess;


import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    void clear() throws DataAccessException;
    void createGame(String name) throws DataAccessException;
    GameData getGame(int id) throws DataAccessException;
    ArrayList<GameData> listGames() throws DataAccessException;
    void updateGame(int id, GameData game) throws DataAccessException;
    int size();
}