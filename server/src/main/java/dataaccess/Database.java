package dataaccess;
import models.*;

import java.util.ArrayList;


public class Database {

    AuthDAO authDatabase;
    UserDAO userDatabase;
    GameDAO gameDatabase;

    public Database() {
        authDatabase = new MemoryAuthDAO();
        userDatabase = new MemoryUserDAO();
        gameDatabase = new MemoryGameDAO();
    }

    public void clearAll() throws DataAccessException {
        authDatabase.clear();
        userDatabase.clear();
        gameDatabase.clear();
    }


    public boolean isAllEmpty() {
        return (authDatabase.size() == 0) && (userDatabase.size() == 0) && (gameDatabase.size() == 0);
    }

    public boolean isGameEmpty(int gameID) throws DataAccessException {
        return gameDatabase.getGame(gameID) == null;
    }

    public boolean isUserEmpty(String name) throws DataAccessException {
        return userDatabase.getUser(name) == null;
    }
    public boolean isAuthEmpty(String token) throws DataAccessException {
        return authDatabase.getAuth(token) == null;
    }

    public void createUser(String name, String password, String email) throws DataAccessException {
        if(isUserEmpty(name))
            userDatabase.createUser(name, new UserData(name, password, email));
        else
            throw new DataAccessException("Username already exists");
    }

    public void createAuth(String token, String name) throws DataAccessException {
        if (isAuthEmpty(token))
            authDatabase.createAuth(token, new AuthData(token, name));
        else
            throw new DataAccessException("Auth taken");
    }

    public void createGame(String name) throws DataAccessException {
        if (isGameEmpty(Integer.parseInt(name)))
            gameDatabase.createGame(name);
        else
            throw new DataAccessException("Game taken");
    }

    public UserData getUser(String name) throws DataAccessException {
        if (isUserEmpty(name))
            throw new DataAccessException("Not valid Username");
        else
            return userDatabase.getUser(name);
    }

    public AuthData getAuth(String token) throws DataAccessException {
        if (isAuthEmpty(token))
            throw new DataAccessException("Not valid token");
        else
            return authDatabase.getAuth(token);
    }

    public GameData getGame(int gameID) throws DataAccessException {
        if (isGameEmpty(gameID))
            throw new DataAccessException("Not valid GameID");
        else
            return gameDatabase.getGame(gameID);
    }

    public GameData getGameName(String name) throws DataAccessException {
        ArrayList<GameData> gameList = gameDatabase.listGames();
        if (gameList.isEmpty())
            throw new DataAccessException("Not valid Game Name");
        for (GameData game : gameList) {
            if (game.gameName().equals(name))
                return game;
        }
        throw new DataAccessException("Not valid Game Name");
    }

    public ArrayList<GameData> getGameList() throws DataAccessException {
        return gameDatabase.listGames();
    }


    public String getPlayerFromColor(GameData game, String color) {
        if (color.equals("WHITE"))
            return game.whiteUsername();
        return game.blackUsername();
    }

    public void updateGame(GameData newGame) throws DataAccessException {
        gameDatabase.updateGame(newGame.gameID(), newGame);
    }

    public boolean noGameName(String name) {
        try {
            getGameName(name);
            return false;
        } catch (DataAccessException ex) {
            return true;
        }
    }

    public void deleteAuth(String token) throws DataAccessException {
        if (isAuthEmpty(token))
            throw new DataAccessException("Not valid token");
        else
            authDatabase.deleteAuth(token);
    }


}
