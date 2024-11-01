package dataaccess;

import models.*;
import java.util.ArrayList;

public class Database {
    private AuthDAO authDataBase;
    private UserDAO userDataBase;
    private GameDAO gameDataBase;

    public Database() {
        authDataBase = new MemoryAuthDAO();
        userDataBase = new SQLUserDAO();
        gameDataBase = new SQLGameDAO();
    }

    public void clearAll() throws DataAccessException {
        authDataBase.clear();
        userDataBase.clear();
        gameDataBase.clear();
    }

    public boolean isAllEmpty() {
        return authDataBase.size() == 0 && userDataBase.size() == 0 && gameDataBase.size() == 0;
    }

    public void createUser(String name, String password, String email) throws DataAccessException {
        if (isUserEmpty(name)) {
            userDataBase.createUser(name, new UserData(name, password, email));
        }
        else
            throw new DataAccessException("Error: Username already taken");
    }

    public void createGame(String name) throws DataAccessException {
        if (isGameEmpty(name)) {
            gameDataBase.createGame(name);
        }
        else
            throw new DataAccessException("Error: Game name already taken");
    }

    public void createAuth(String token, String name) throws DataAccessException {
        if (isAuthEmpty(token)) {
            authDataBase.createAuth(token, new AuthData(token, name));
        }
        else
            throw new DataAccessException("Error: Auth token already taken");
    }

    public AuthData getAuth(String token) throws DataAccessException {
        AuthData authData = authDataBase.getAuth(token);
        if (authData == null) {
            throw new DataAccessException("Error: Invalid token");
        }
        return authData;
    }

    public UserData getUser(String name) throws DataAccessException {
        UserData userData = userDataBase.getUser(name);
        if (userData == null) {
            throw new DataAccessException("Error: Invalid username");
        }
        return userData;
    }

    public GameData getGame(int gameID) throws DataAccessException {
        GameData gameData = gameDataBase.getGame(gameID);
        if (gameData == null) {
            throw new DataAccessException("Error: Invalid game ID");
        }
        return gameData;
    }

    public GameData getGameName(String name) throws DataAccessException {
        for (GameData game : gameDataBase.listGames())
            if (game.gameName().equals(name)) {
                return game;
            }
        throw new DataAccessException("Error: Invalid game name");
    }

    public boolean noGameName(String name) {
        try {
            getGameName(name);
            return false;
        } catch (DataAccessException e) {
            return true;
        }
    }

    public ArrayList<GameData> getGameList() throws DataAccessException {
        return gameDataBase.listGames();
    }

    public String getPlayerFromColor(GameData game, String color) {
        return "WHITE".equals(color) ? game.whiteUsername() : game.blackUsername();
    }

    public boolean isAuthEmpty(String token) throws DataAccessException {
        return authDataBase.getAuth(token) == null;
    }

    public boolean isUserEmpty(String name) throws DataAccessException {
        return userDataBase.getUser(name) == null;
    }


    public boolean isGameEmpty(String name) {
        try {
            getGameName(name);
            return false;
        } catch (DataAccessException e) {
            return true;
        }
    }

    public void deleteAuth(String token) throws DataAccessException {
        if (isAuthEmpty(token)) {throw new DataAccessException("Error: Invalid token");}
        authDataBase.deleteAuth(token);
    }

    public void updateGame(GameData newGame) throws DataAccessException {
        gameDataBase.updateGame(newGame.gameID(), newGame);
    }
}