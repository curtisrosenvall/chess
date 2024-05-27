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


    public void createUser(String name, String password, String email) throws DataAccessException {
        if(isUserEmpty(name))
            userDatabase.createUser(name, new UserData(name, password, email));
        else
            throw new DataAccessException("Username already exists");
    }


}