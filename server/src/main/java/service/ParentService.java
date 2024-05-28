package service;

import dataaccess.*;

public class ParentService {

    AuthDAO authDB;
    GameDAO gameDB;
    UserDAO userDB;

    public ParentService() {
        authDB = new MemoryAuthDAO();
        userDB = new MemoryUserDAO();
        gameDB = new MemoryGameDAO();
    }

    public AuthDAO getAuthDB(){
        return authDB;
    }

    public UserDAO getUserDB(){
        return userDB;
    }

    public GameDAO getGameDB(){
        return gameDB;
    }

    public void setGameDB(GameDAO gameDB){
        this.gameDB = gameDB;
    }

    public void setAuthDB(AuthDAO authDB){
        this.authDB = authDB;
    }

    public void setUserDB(UserDAO userDB){
        this.userDB = userDB;
    }
}
