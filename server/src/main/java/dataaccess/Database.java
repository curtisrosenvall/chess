package dataaccess;
import models.*;


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

    public void deleteAuth(String token) throws DataAccessException {
        if (isAuthEmpty(token))
            throw new DataAccessException("Not valid token");
        else
            authDatabase.deleteAuth(token);
    }


}
