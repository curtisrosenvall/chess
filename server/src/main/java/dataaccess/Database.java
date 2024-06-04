package dataaccess;
import model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class Database {

    // DAO objects for authentication, user, and game data access
    AuthDAO authDataBase;
    UserDAO userDataBase;
    GameDAO gameDataBase;

    // Constructor initializes in-memory DAO implementations
    public Database() {
        createTables();
        authDataBase = new SQLAuthDAO();
        userDataBase = new SQLUserDAO();
        gameDataBase = new SQLGameDAO();
    }

    private void createTables() {
        try {
            DatabaseManager.createDatabase();
        } catch (Exception e) {
           System.out.println("Database creation failed");
        }
        try(Connection connection = DatabaseManager.getConnection()) {
            String createUserTable = """
                    CREATE TABLE IF NOT EXISTS user (
                        username VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL,
                        json TEXT NOT NULL,
                        PRIMARY KEY (username)
                    )""";
            PreparedStatement createUserStatement = connection.prepareStatement(createUserTable);
            createUserStatement.executeUpdate();

            String createGameTable = """
                    CREATE TABLE IF NOT EXISTS game (
                        gameID INT NOT NULL AUTO_INCREMENT,
                        whiteUsername VARCHAR(255) DEFAULT NULL,
                        blackUsername VARCHAR(255) DEFAULT NULL,
                        gameName VARCHAR(255) NOT NULL,
                        game TEXT NOT NULL,
                        PRIMARY KEY (gameID)
                    )""";
            PreparedStatement createGameStatement = connection.prepareStatement(createGameTable);
            createGameStatement.executeUpdate();

            String createAuthTable = """
                    CREATE TABLE IF NOT EXISTS auth (
                        authToken VARCHAR(255) NOT NULL,
                        username VARCHAR(255) NOT NULL,
                        json TEXT NOT NULL,
                        PRIMARY KEY (authToken)
                    )""";
            PreparedStatement createAuthStatement = connection.prepareStatement(createAuthTable);
            createAuthStatement.executeUpdate();
        } catch(Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }

    }

    // Clears all data from the DAOs
    public void clearAll() throws DataAccessException {
        authDataBase.clear();
        userDataBase.clear();
        gameDataBase.clear();
    }

    // Checks if all DAOs are empty
    public boolean isAllEmpty() {
        return (authDataBase.size() == 0) && (userDataBase.size() == 0) && (gameDataBase.size() == 0);
    }

    // Create a new user if the username is not already taken
    public void createUser(String name, String password, String email) throws DataAccessException {
        if (isUserEmpty(name))
            userDataBase.createUser(name, new UserData(name, password, email));
        else
            throw new DataAccessException("Error: already taken");
    }

    // Create a new game if the game name is not already taken
    public void createGame(String name) throws DataAccessException {
        if (isGameEmpty(name))
            gameDataBase.createGame(name);
        else
            throw new DataAccessException("Game taken");
    }

    // Create a new authentication token if the token is not already taken
    public void createAuth(String token, String name) throws DataAccessException {
        if (isAuthEmpty(token))
            authDataBase.createAuth(token, new AuthData(token, name));
        else
            throw new DataAccessException("Auth taken");
    }

    // Retrieve authentication data for a given token
    public AuthData getAuth(String token) throws DataAccessException {
        if (isAuthEmpty(token))
            throw new DataAccessException("Not valid token");
        else
            return authDataBase.getAuth(token);
    }

    // Retrieve user data for a given username
    public UserData getUser(String name) throws DataAccessException {
        if (isUserEmpty(name))
            throw new DataAccessException("Not valid Username");
        else
            return userDataBase.getUser(name);
    }

    // Retrieve game data for a given game ID
    public GameData getGame(int gameID) throws DataAccessException {
        if (isGameEmpty(gameID))
            throw new DataAccessException("Not valid GameID");
        else
            return gameDataBase.getGame(gameID);
    }

    // Retrieve game data for a given game name
    public GameData getGameName(String name) throws DataAccessException {
        ArrayList<GameData> gameList = gameDataBase.listGames();
        if (gameList.isEmpty())
            throw new DataAccessException("Not valid Game Name");
        for (GameData game : gameList) {
            if (game.gameName().equals(name))
                return game;
        }
        throw new DataAccessException("Not valid Game Name");
    }

    // Check if a game name is not in use
    public boolean noGameName(String name) {
        try {
            getGameName(name);
            return false;
        } catch (DataAccessException ex) {
            return true;
        }
    }

    // Retrieve a list of all games
    public ArrayList<GameData> getGameList() throws DataAccessException {
        return gameDataBase.listGames();
    }

    // Retrieve the player username for a given color in a game
    public String getPlayerFromColor(GameData game, String color) {
        if (color.equals("WHITE"))
            return game.whiteUsername();
        return game.blackUsername();
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
            return getGameName(name) == null;
        } catch (DataAccessException ex) {
            return true;
        }
    }

    // Delete authentication data for a given token
    public void deleteAuth(String token) throws DataAccessException {
        if (isAuthEmpty(token))
            throw new DataAccessException("Not valid token");
        else
            authDataBase.deleteAuth(token);
    }

    // Update game data for a given game
    public void updateGame(GameData newGame) throws DataAccessException {
        gameDataBase.updateGame(newGame.gameID(), newGame);
    }
}