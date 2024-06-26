package handler;

import dataaccess.DataAccessException;
import dataaccess.*;
import model.UserData;
import request.LoginRequest;
import result.LoginResult;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class Login implements Route {

    Database database;
    MethodHandlers methodHandlers;

    public Login(Database database) {
        this.database = database;
        methodHandlers = new MethodHandlers();
    }

    @Override
    public Object handle(Request request, Response response) {
        LoginRequest loginRequest;
        UserData user;
        try {
            loginRequest = (LoginRequest) methodHandlers.getBody(request, "LoginRequest");
            methodHandlers.isNullString(loginRequest.getUsername());
            methodHandlers.isNullString(loginRequest.getPassword());
        } catch(DataAccessException ex) {
            return methodHandlers.getResponse(response, 401, new LoginResult(null, "Error: bad request", null, null));
        }
        try {
            user = database.getUser(loginRequest.getUsername());
            UserService login = new UserService(database);
            LoginResult loginResult = login.loginUser(loginRequest);
            if(loginResult.isSuccess()) {
                return methodHandlers.getResponse(response, 200, loginResult);
            } else if(loginResult.getMessage().contains("Unauthorized")) {
                return methodHandlers.getResponse(response, 401, new LoginResult(null, "Error: unauthorized", null, null));
            } else {
                return methodHandlers.getResponse(response, 500, loginResult);
            }
        } catch(DataAccessException ex) {
            return methodHandlers.getResponse(response, 401, new LoginResult(null, "Error: unauthorized", null, null));
        }
    }
}