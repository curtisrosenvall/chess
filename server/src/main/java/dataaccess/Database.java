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

    public boolean isUserEmpty(String name) throws DataAccessException {
        return userDatabase.getUser(name) == null;
    }

    public void createUser(String name, String password, String email) throws DataAccessException {
        if(isUserEmpty(name))
            userDatabase.createUser(name, new UserData(name, password, email));
        else
            throw new DataAccessException("Username already exists");
    }
}
