package handler;
import dataaccess.DataAccessException;
import dataaccess.Database;
import spark.Route;
import spark.Request;
import spark.Response;
import request.*;
import response.*;
import model.*;
import service.*;


public class Login implements Route {

    Database database;
    methodHandlers methodHandlers;

    public Login(Database database) {
        this.database = database;
        methodHandlers = new methodHandlers();
    }

    @Override
    public Object handle(Request request, Response response) {
        LoginRequest loginRequest;
        UserData user;
        try {
            loginRequest = (LoginRequest) methodHandlers.getBody(request, "LoginRequest");
            methodHandlers.isNullString(loginRequest.getUsername());
            methodHandlers.isNullString(loginRequest.getPassword());
            user = database.getUser(loginRequest.getUsername());
        } catch(DataAccessException ex) {
            return methodHandlers.getResponse(response, 500, new LoginResponse(null, "Error: bad request", null, null));
        }
        try {
            UserService login = new UserService(database);
            LoginResponse loginResult = login.loginUser(loginRequest);
            if(user.password().equals(loginRequest.getPassword())) {
                if(loginResult.isSuccess()) {
                    return methodHandlers.getResponse(response, 200, loginResult);
                } else {
                    return methodHandlers.getResponse(response, 500, loginResult);
                }
            } else
                throw new DataAccessException("Error: unauthorized");
        } catch(DataAccessException ex) {
            return methodHandlers.getResponse(response, 401, new LoginResponse(null, "Error: unauthorized", null, null));
        }
    }


}
