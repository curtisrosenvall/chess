package handler;
import dataaccess.Database;
import spark.Response;
import spark.Request;
import spark.Route;
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
            // Attempt to parse the request and validate the fields
            loginRequest = (LoginRequest) methodHandlers.getBody(request, "LoginRequest");
            methodHandlers.isNullString(loginRequest.getUsername());
            methodHandlers.isNullString(loginRequest.getPassword());

            user = database.getUser(loginRequest.getUsername());
            if (user == null || !user.password().equals(loginRequest.getPassword())) {
                throw new UnauthorizedAccessException("Error: unauthorized");
            }

            // Attempt to log the user in
            UserService login = new UserService(database);
            LoginResponse loginResult = login.loginUser(loginRequest);
            if (loginResult.isSuccess()) {
                return methodHandlers.getResponse(response, 200, loginResult);
            } else {
                return methodHandlers.getResponse(response, 500, loginResult);
            }
        } catch (IllegalArgumentException ex) {
            // Catch any issues with the request format or missing fields
            return methodHandlers.getResponse(response, 400, new LoginResponse(null, "Error: bad request", null, null));
        } catch (UnauthorizedAccessException ex) {
            // Catch unauthorized access attempts
            return methodHandlers.getResponse(response, 401, new LoginResponse(null, "Error: unauthorized", null, null));
        } catch (Exception ex) {
            // Catch any other unexpected exceptions
            return methodHandlers.getResponse(response, 500, new LoginResponse(null, "Error: internal server error", null, null));
        }
    }

    // Define custom exception for unauthorized access
    private class UnauthorizedAccessException extends Exception {
        public UnauthorizedAccessException(String message) {
            super(message);
        }
    }
}
