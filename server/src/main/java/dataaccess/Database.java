package dataaccess;

import models.*;
import java.util.ArrayList;

/**
 * The Database class provides data access methods for authentication, user, and game data.
 */
public class Database {

    // DAO objects for authentication, user, and game data access
    private AuthDAO authDataBase;
    private UserDAO userDataBase;
    private GameDAO gameDataBase;

    // Constructor initializes in-memory DAO implementations
    public Database() {
        authDataBase = new MemoryAuthDAO();
        userDataBase = new MemoryUserDAO();
        gameDataBase = new MemoryGameDAO();
    }

    // Clears all data from the DAOs
    public void clearAll() throws DataAccessException {
        authDataBase.clear();
        userDataBase.clear();
        gameDataBase.clear();
    }

    // Checks if all DAOs are empty
    public boolean isAllEmpty() {
        return authDataBase.size() == 0
                && userDataBase.size() == 0
                && gameDataBase.size() == 0;
    }

    // Create a new user if the username is not already taken
    public void createUser(String name, String password, String email) throws DataAccessException {
        if (isUserEmpty(name)) {
            UserData userData = new UserData(name, password, email);
            userDataBase.createUser(name, userData);
        } else {
            throw new DataAccessException("Error: Username already taken");
        }
    }

    // Create a new game if the game name is not already taken
    public void createGame(String name) throws DataAccessException {
        if (isGameEmpty(name)) {
            gameDataBase.createGame(name);
        } else {
            throw new DataAccessException("Error: Game name already taken");
        }
    }

    // Create a new authentication token if the token is not already taken
    public void createAuth(String token, String name) throws DataAccessException {
        if (isAuthEmpty(token)) {
            AuthData authData = new AuthData(token, name);
            authDataBase.createAuth(token, authData);
        } else {
            throw new DataAccessException("Error: Auth token already taken");
        }
    }

    // Retrieve authentication data for a given token
    public AuthData getAuth(String token) throws DataAccessException {
        AuthData authData = authDataBase.getAuth(token);
        if (authData == null) {
            throw new DataAccessException("Error: Invalid token");
        }
        return authData;
    }

    // Retrieve user data for a given username
    public UserData getUser(String name) throws DataAccessException {
        UserData userData = userDataBase.getUser(name);
        if (userData == null) {
            throw new DataAccessException("Error: Invalid username");
        }
        return userData;
    }

    // Retrieve game data for a given game ID
    public GameData getGame(int gameID) throws DataAccessException {
        GameData gameData = gameDataBase.getGame(gameID);
        if (gameData == null) {
            throw new DataAccessException("Error: Invalid game ID");
        }
        return gameData;
    }

    // Retrieve game data for a given game name
    public GameData getGameName(String name) throws DataAccessException {
        for (GameData game : gameDataBase.listGames()) {
            if (game.gameName().equals(name)) {
                return game;
            }
        }
        throw new DataAccessException("Error: Invalid game name");
    }

    // Check if a game name is not in use
    public boolean noGameName(String name) {
        try {
            getGameName(name);
            return false;
        } catch (DataAccessException e) {
            return true;
        }
    }

    // Retrieve a list of all games
    public ArrayList<GameData> getGameList() throws DataAccessException {
        return gameDataBase.listGames();
    }

    // Retrieve the player username for a given color in a game
    public String getPlayerFromColor(GameData game, String color) {
        if ("WHITE".equals(color)) {
            return game.whiteUsername();
        } else {
            return game.blackUsername();
        }
    }

    // Check if a given authentication token exists
    public boolean isAuthEmpty(String token) throws DataAccessException {
        return authDataBase.getAuth(token) == null;
    }

    // Check if a given username exists
    public boolean isUserEmpty(String name) throws DataAccessException {
        return userDataBase.getUser(name) == null;
    }

    // Check if a given game ID exists
    public boolean isGameEmpty(int gameID) throws DataAccessException {
        return gameDataBase.getGame(gameID) == null;
    }

    // Check if a given game name exists
    public boolean isGameEmpty(String name) {
        try {
            getGameName(name);
            return false;
        } catch (DataAccessException e) {
            return true;
        }
    }

    // Delete authentication data for a given token
    public void deleteAuth(String token) throws DataAccessException {
        if (isAuthEmpty(token)) {
            throw new DataAccessException("Error: Invalid token");
        }
        authDataBase.deleteAuth(token);
    }

    // Update game data for a given game
    public void updateGame(GameData newGame) throws DataAccessException {
        gameDataBase.updateGame(newGame.gameID(), newGame);
    }
}