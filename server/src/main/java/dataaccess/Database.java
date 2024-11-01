package dataaccess;

import models.*;

import java.sql.*;
import java.util.ArrayList;

public class Database {
     AuthDAO authDataBase;
     UserDAO userDataBase;
     GameDAO gameDataBase;

    public Database() {
        createTables();
        authDataBase = new SQLAuthDAO();
        userDataBase = new SQLUserDAO();
        gameDataBase = new SQLGameDAO();
    }

    private void createTables() {
        try {
            DatabaseManager.createDatabase();
        } catch (Exception ex) {
            System.out.println("Cannot create database");
        }
        try(Connection conn = DatabaseManager.getConnection()) {
            String createUserTable = """
                            CREATE TABLE IF NOT EXISTS user (
                            username VARCHAR(255) NOT NULL,
                            password VARCHAR(255) NOT NULL,
                            email VARCHAR(255) NOT NULL,
                            json TEXT NOT NULL,
                            PRIMARY KEY (username)
                    )""";
            PreparedStatement createUserStatement = conn.prepareStatement(createUserTable);
            createUserStatement.executeUpdate();

            String createAuthTable = """
                    CREATE TABLE IF NOT EXISTS auth (
                        authToken VARCHAR(255) NOT NULL,
                        username VARCHAR(255) NOT NULL,
                        json TEXT NOT NULL,
                        PRIMARY KEY (authToken)
                    )""";
            PreparedStatement createAuthStatement = conn.prepareStatement(createAuthTable);
            createAuthStatement.executeUpdate();

            String createGameTable = """
                    CREATE TABLE IF NOT EXISTS game (
                        gameID INT NOT NULL AUTO_INCREMENT,
                        whiteUsername VARCHAR(255) DEFAULT NULL,
                        blackUsername VARCHAR(255) DEFAULT NULL,
                        gameName VARCHAR(255) NOT NULL,
                        game TEXT NOT NULL,
                        PRIMARY KEY (gameID)
                    )""";
            PreparedStatement createGameStatement = conn.prepareStatement(createGameTable);
            createGameStatement.executeUpdate();


        } catch(Exception ex) {
            System.out.println("Error" + ex.getMessage());
        }
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