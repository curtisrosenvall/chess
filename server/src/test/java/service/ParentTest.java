package service;

import request.*;
import result.*;
import dataaccess.Database;

public class ParentTest {

    Database db;
    UserService userService;
    GameService gameService;
    RegisterRes registerResult;
    String authToken;

    public void createDataBase() {
        db = new Database();
        userService = new UserService(db);
        gameService = new GameService(db);
        registerResult = userService.createUser(new RegisterReq("Test", "1234", "test@gmail.com"));
        authToken = registerResult.getAuthToken();
    }

}