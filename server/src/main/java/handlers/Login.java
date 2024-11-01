package handlers;

import dataaccess.DataAccessException;
import dataaccess.*;
import models.UserData;
import request.*;
import result.*;
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
        LoginReq loginRequest;
        try {
            loginRequest = (LoginReq) methodHandlers.getBody(request, "LoginRequest");
            methodHandlers.isNullString(loginRequest.getUsername());
            methodHandlers.isNullString(loginRequest.getPassword());
        } catch (DataAccessException ex) {

            return methodHandlers.getResponse(
                    response,
                    401,
                    new LoginRes(null, "Error: bad request", null, null)
            );
        }

        try {
            database.getUser(loginRequest.getUsername());
            UserService loginService = new UserService(database);
            LoginRes loginResult = loginService.loginUser(loginRequest);


            if (loginResult.isSuccess()) {
                return methodHandlers.getResponse(response, 200, loginResult);
            } else if (loginResult.getMessage().contains("Unauthorized")) {
                return methodHandlers.getResponse(
                        response,
                        401,
                        new LoginRes(null, "Error: unauthorized", null, null)
                );
            } else {
                return methodHandlers.getResponse(response, 500, loginResult);
            }
        } catch (DataAccessException ex) {
            return methodHandlers.getResponse(
                    response,
                    401,
                    new LoginRes(null, "Error: unauthorized", null, null)
            );
        }
    }
}