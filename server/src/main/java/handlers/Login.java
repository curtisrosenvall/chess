package handlers;

import dataaccess.DataAccessException;
import dataaccess.Database;
import models.UserData;
import request.*;
import result.*;
import service.UserService;
import spark.*;


public class Login implements Route {

    // Instance of the database
    Database database;
    // Instance of method handlers for processing requests
    MethodHandlers methodHandlers;

    // Constructor to initialize the database and method handlers
    public Login(Database database) {
        this.database = database;
        methodHandlers = new MethodHandlers();
    }

    // Handle method to process the incoming login request
    @Override
    public Object handle(Request request, Response response) {
        LoginReq loginRequest;
        UserData user;
        try {
            // Parse the request body to create a LoginReq object
            loginRequest = (LoginReq) methodHandlers.getBody(request, "LoginReq");
            // Check if the username and password are null or empty
            methodHandlers.isNullString(loginRequest.getUsername());
            methodHandlers.isNullString(loginRequest.getPassword());
        } catch (DataAccessException ex) {
            // Return a 401 error response if there was an issue with the request data
            return methodHandlers.getResponse(response, 401, new LoginRes(null, "Error: bad request", null, null));
        }
        try {
            // Retrieve the user data for the given username
            user = database.getUser(loginRequest.getUsername());
            // Create a new UserService instance to handle user login
            UserService login = new UserService(database);
            // Call the loginUser method and get the result
            LoginRes loginResult = login.loginUser(loginRequest);
            // Check if the provided password matches the stored password
            if (user.password().equals(loginRequest.getPassword())) {
                if (loginResult.isSuccess()) {
                    // Return a 200 response if the login was successful
                    return methodHandlers.getResponse(response, 200, loginResult);
                } else {
                    // Return a 500 error response if there was an issue with the login process
                    return methodHandlers.getResponse(response, 500, loginResult);
                }
            } else {
                // Throw an exception if the password is incorrect
                throw new DataAccessException("Error: unauthorized");
            }
        } catch (DataAccessException ex) {
            // Return a 401 error response if the username is invalid or unauthorized
            return methodHandlers.getResponse(response, 401, new LoginRes(null, "Error: unauthorized", null, null));
        }
    }
}