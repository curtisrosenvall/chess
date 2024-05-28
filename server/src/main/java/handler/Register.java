package handler;

import dataaccess.DataAccessException;
import dataaccess.Database;
import response.RegisterResponse;
import spark.Request;
import spark.Response;
import request.*;
import service.*;
import spark.Route;

public class Register implements Route {
    Database database;
    methodHandlers methodHandlers;

    public Register(Database database) {
        this.database = database;
        methodHandlers = new methodHandlers();
    }

    public Object handle(Request request, Response response) {
        RegisterRequest registerRequest;
        try {
            registerRequest = (RegisterRequest) methodHandlers.getBody(request, "RegisterRequest");
            methodHandlers.isNullString(registerRequest.getUsername());
            methodHandlers.isNullString(registerRequest.getPassword());
            methodHandlers.isNullString(registerRequest.getEmail());
        } catch(DataAccessException ex) {
            return methodHandlers.getResponse(response,400, new RegisterResponse(null, "Error: Bad Request", null, null));
        }
        UserService register = new UserService(database);
        RegisterResponse registerResult = register.createUser(registerRequest);
        if(registerResult.isSuccess()) {
            return methodHandlers.getResponse(response, 200, registerResult);
        } else if(registerResult.getMessage().contains("already taken")) {
            return methodHandlers.getResponse(response, 403, registerResult);
        } else
            return methodHandlers.getResponse(response, 500, registerResult);
    }
}
