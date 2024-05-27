package dataaccess;
import java.util.ArrayList;
import model.AuthData;
import model.UserData;
import model.GameData;


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


    public boolean isUserEmpty(String name) throws DataAccessException {
        return userDatabase.getUser(name) == null;
    }


    public boolean isAuthEmpty(String token) throws DataAccessException {
        return authDatabase.getAuth(token) == null;
    }


    public boolean isGameEmpty(int id) throws DataAccessException {
        return gameDatabase.getGame(id) == null;
    }


    public boolean isGameNameEmpty(String name) throws DataAccessException {
        try {
            return getGameName(name) == null;
        } catch(DataAccessException exception){
            return true;
        }
    }



//    Create

    public void createUser(String name, String password, String email) throws DataAccessException {
        if(isUserEmpty(name))
            userDatabase.createUser(name, new UserData(name, password, email));
        else
            throw new DataAccessException("Username already exists");
    }

    public void createGame(String name) throws DataAccessException {
        if(isGameNameEmpty(name))
            gameDatabase.createGame(name);
        else
            throw new DataAccessException("Game already exists");
    }

    public void createAuth(String token, String name) throws DataAccessException {
        if(isAuthEmpty(token))
            authDatabase.createAuth(token, new AuthData(token,name));
        else
            throw new DataAccessException("Auth already exists");

    }

//    Get
    public AuthData getAuth(String token) throws DataAccessException {
        if(isAuthEmpty(token))
            throw new DataAccessException("Not valid token");
        else
            return authDatabase.getAuth(token);
    }

    public UserData getUser(String token) throws DataAccessException {
        if(isUserEmpty(token))
            throw new DataAccessException("Not valid Username");
        else
            return userDatabase.getUser(token);
    }

    public AuthData getAuthName(String name) throws DataAccessException {
        ArrayList<AuthData> authList = authDatabase.getAllAuths();
        for(AuthData auth : authList){
            if(auth.username().equals(name))
                return auth;
        }
        throw new DataAccessException("Not valid Username");
    }

    public GameData getGame(int id) throws DataAccessException {
        if(isGameEmpty(id))
            throw new DataAccessException("Not valid id");
        else
            return gameDatabase.getGame(id);
    }

    public GameData getGameName(String name) throws DataAccessException {
        ArrayList<GameData> gameList = gameDatabase.getAllGames();
        for (GameData game: gameList){
            if(game.gameName().equals(name))
                return game;
        }
        throw new DataAccessException("Not valid Game Name");
    }

    public ArrayList<GameData> getGameList() throws DataAccessException {
        return gameDatabase.getAllGames();
    }

    public boolean noGameName(String name) {
        try {
            getGameName(name);
            return false;
        } catch(DataAccessException ex) {
            return true;
        }
    }

    public String getPlayerFromColor(GameData game, String color) {
        if(color.equals("WHITE"))
            return game.whiteUsername();
        return game.blackUsername();
    }

//    delete
    public void deleteAuth(String token) throws DataAccessException {
        if(isAuthEmpty(token))
            throw new DataAccessException("Not valid token");
        else
            authDatabase.deleteAuth(token);
    }

    public void deleteUser(String name) throws DataAccessException {
        if(isUserEmpty(name))
            throw new DataAccessException("Not valid Username");
        else
            userDatabase.deleteUser(name);
    }

    public void deleteGame(int id) throws DataAccessException {
        if(isGameEmpty(id))
            throw new DataAccessException("Not valid id");
        else
            gameDatabase.deleteGame(id);
    }

    public void updateGame(GameData newGame) throws DataAccessException {
        gameDatabase.updateGame(newGame.gameId(), newGame);
    }

    public AuthDAO getAuthDataBase() { return authDatabase; }
    public void setAuthDataBase(AuthDAO authDataBase) { this.authDatabase = authDataBase; }
    public UserDAO getUserDataBase() { return userDatabase; }
    public void setUserDataBase(UserDAO userDataBase) { this.userDatabase = userDataBase; }
    public GameDAO getGameDataBase() { return gameDatabase; }
    public void setGameDataBase(GameDAO gameDataBase) { this.gameDatabase = gameDataBase; }
}


