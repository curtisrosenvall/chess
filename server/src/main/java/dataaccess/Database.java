package dataaccess;



import model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import org.eclipse.jetty.websocket.api.Session;

public class Database {

    AuthDAO authDataBase;
    UserDAO userDataBase;
    GameDAO gameDataBase;
    HashMap<Integer, ArrayList<Session>> sessionMap;

    public Database() {
        createTables();

        authDataBase = new SQLAuthDAO();
        userDataBase = new SQLUserDAO();
        gameDataBase = new SQLGameDAO();

        sessionMap = new HashMap<>();
    }

    public void createTables() {
        //Create auth, user, and game table if not created already.
        try {
            DatabaseManager.createDatabase();
        } catch(Exception ex) {
            System.out.println("You messed up Curtis");
        }
        try (Connection conn = DatabaseManager.getConnection()) {
            /* Drop Database code
            var dropDbStatement = conn.prepareStatement("DROP DATABASE IF EXISTS chess");
            dropDbStatement.executeUpdate();
             */
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

            String createAuthTable = """
                    CREATE TABLE IF NOT EXISTS auth (
                        authToken VARCHAR(255) NOT NULL,
                        username VARCHAR(255) NOT NULL,
                        json TEXT NOT NULL,
                        PRIMARY KEY (authToken)
                    )""";
            PreparedStatement createAuthStatement = conn.prepareStatement(createAuthTable);
            createAuthStatement.executeUpdate();
        } catch(Exception ex) {
            //I need to do something with this error, just not sure what to do.
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public void clearAll() throws DataAccessException {
        authDataBase.clear();
        userDataBase.clear();
        gameDataBase.clear();
    }
    public boolean isAllEmpty() {
        return (authDataBase.size() == 0) && (userDataBase.size() == 0) && (gameDataBase.size() == 0);
    }

    //Create Methods
    public void createUser(String name, String password, String email) throws DataAccessException {
        if(isUserEmpty(name))
            userDataBase.createUser(name, new UserData(name, password, email));
        else
            throw new DataAccessException("Error: already taken");
    }
    public void createGame(String name) throws DataAccessException {
        if(isGameEmpty(name))
            gameDataBase.createGame(name);
        else
            throw new DataAccessException("Game taken");
    }
    public void createAuth(String token, String name) throws DataAccessException {
        if(isAuthEmpty(token))
            authDataBase.createAuth(token, new AuthData(token, name));
        else
            throw new DataAccessException("Auth taken");
    }

    //Get Methods
    public AuthData getAuth(String token) throws DataAccessException {
        if(isAuthEmpty(token))
            throw new DataAccessException("Not valid token");
        else
            return authDataBase.getAuth(token);
    }
    public UserData getUser(String name) throws DataAccessException {
        if(isUserEmpty(name))
            throw new DataAccessException("Not valid Username");
        else
            return userDataBase.getUser(name);
    }
    public GameData getGame(int gameID) throws DataAccessException {
        if(isGameEmpty(gameID))
            throw new DataAccessException("Not valid GameID");
        else
            return gameDataBase.getGame(gameID);
    }
    public GameData getGameName(String name) throws DataAccessException {
        ArrayList<GameData> gameList = gameDataBase.listGames();
        if(gameList.isEmpty())
            throw new DataAccessException("Not valid Game Name");
        for(GameData game : gameList) {
            if(game.gameName().equals(name))
                return game;
        }
        throw new DataAccessException("Not valid Game Name");
    }
    public boolean noGameName(String name) {
        try {
            getGameName(name);
            return false;
        } catch(DataAccessException ex) {
            return true;
        }
    }
    public ArrayList<GameData> getGameList() throws DataAccessException {
        return gameDataBase.listGames();
    }
    public String getPlayerFromColor(GameData game, String color) {
        if(color.equals("WHITE"))
            return game.whiteUsername();
        return game.blackUsername();
    }

    //is_Empty
    public boolean isAuthEmpty(String token) throws DataAccessException {
        return authDataBase.getAuth(token) == null;
    }
    public boolean isUserEmpty(String name) throws DataAccessException {
        return userDataBase.getUser(name) == null;
    }
    public boolean isGameEmpty(int gameID) throws DataAccessException {
        return gameDataBase.getGame(gameID) == null;
    }
    public boolean isGameEmpty(String name) {
        try {
            return getGameName(name) == null;
        } catch(DataAccessException ex) {
            return true;
        }
    }

    //Delete Methods
    public void deleteAuth(String token) throws DataAccessException {
        if(isAuthEmpty(token))
            throw new DataAccessException("Not valid token");
        else
            authDataBase.deleteAuth(token);
    }

    public void updateGame(GameData newGame) throws DataAccessException{
        gameDataBase.updateGame(newGame.gameID(), newGame);
    }

    public void addSession(Integer gameID, Session session) {
        ArrayList<Session> sessionList = sessionMap.get(gameID);
        if(sessionList == null)
            sessionList = new ArrayList<>();
        sessionList.add(session);
        sessionMap.put(gameID, sessionList);
    }

    public ArrayList<Session> getSessionList(Integer gameID) {
        return sessionMap.get(gameID);
    }

    public void removeSession(Integer gameID, Session session) {
        ArrayList<Session> sessionList = sessionMap.get(gameID);
        sessionList.remove(session);
        sessionMap.put(gameID, sessionList);
    }
}