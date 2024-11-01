package handlers;

import dataaccess.DataAccessException;
import dataaccess.Database;
import request.*;
import result.*;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class Register implements Route {

    Database database;
    MethodHandlers methodHandlers;

    public Register(Database database) {
        this.database = database;
        methodHandlers = new MethodHandlers();
    }

    @Override
    public Object handle(Request request, Response response) {
        RegisterReq registerRequest;
        try {
            registerRequest = (RegisterReq) methodHandlers.getBody(request, "RegisterRequest");
            methodHandlers.isNullString(registerRequest.getUsername());
            methodHandlers.isNullString(registerRequest.getPassword());
            methodHandlers.isNullString(registerRequest.getEmail());
        } catch(DataAccessException ex) {
            return methodHandlers.getResponse(response,400, new RegisterRes(null, "Error: Bad Request", null, null));
        }
        UserService register = new UserService(database);
        RegisterRes registerResult = register.createUser(registerRequest);
        if(registerResult.isSuccess()) {
            return methodHandlers.getResponse(response, 200, registerResult);
        } else if(registerResult.getMessage().contains("already taken")) {
            return methodHandlers.getResponse(response, 403, registerResult);
        } else
            return methodHandlers.getResponse(response, 500, registerResult);
    }
}