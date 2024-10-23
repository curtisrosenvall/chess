package handlers;

import dataaccess.DataAccessException;

import dataaccess.Database;
import models.UserData;
import request.*;
import result.*;
import service.*;
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
        LoginReq loginReq;
        UserData user;
        try {
            loginReq = (LoginReq) methodHandlers.getBody(request, "LoginRequest");
            methodHandlers.isNullString(loginReq.getUsername());
            methodHandlers.isNullString(loginReq.getPassword());
        } catch(DataAccessException ex) {
            return methodHandlers.getResponse(response, 401, new LoginRes(null, "Error: bad request", null, null));
        }
        try {
            user = database.getUser(loginReq.getUsername());
            UserService login = new UserService(database);
            LoginRes loginResult = login.loginUser(loginReq);
            if(user.password().equals(loginReq.getPassword())) {
                if(loginResult.isSuccess()) {
                    return methodHandlers.getResponse(response, 200, loginResult);
                } else {
                    return methodHandlers.getResponse(response, 500, loginResult);
                }
            } else
                throw new DataAccessException("Error: unauthorized");
        } catch(DataAccessException ex) {
            return methodHandlers.getResponse(response, 401, new LoginRes(null, "Error: unauthorized", null, null));
        }
    }
}