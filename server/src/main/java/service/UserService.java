package service;

import dataaccess.DataAccessException;
import dataaccess.Database;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import model.*;
import response.LoginResponse;
import response.LogoutResponse;
import response.RegisterResponse;
import java.util.UUID;

public class UserService {

    Database database;

    public UserService(Database database) {
        this.database = database;
    }

    public RegisterResponse createUser(RegisterRequest request) {
        String name = request.getUsername();
        String password = request.getPassword();
        String email = request.getEmail();
        RegisterResponse result;
        try {
            database.createUser(name, password,email);
            String authToken = newAuthToken();
            database.createAuth(authToken, name);
            result = new RegisterResponse(true, null, name, authToken);
        } catch(DataAccessException ex) {
            result = new RegisterResponse(false, "Unable to create user", null, null);
        }
        return result;
    }

    public LoginResponse loginUser(LoginRequest request) {
        String name = request.getUsername();
        String password = request.getPassword();
        LoginResponse result;
        try {
            UserData user = database.getUser(name);
            if(user.password().equals(password)) {
                if(!database.isAuthNameEmpty(name)) {
                    String authToken = database.getAuthName(name).authToken();
                    database.deleteAuth(authToken);
                }
                String newToken = newAuthToken();
                database.createAuth(newToken, name);
                result = new LoginResponse(true, null, name, newToken);
            }
            else
                throw new DataAccessException("Invalid Password");
        } catch(DataAccessException ex) {
            result = new LoginResponse(false, ex.getMessage(), null, null);
        }
        return result;
    }

    public LogoutResponse logoutUser(LogoutRequest request) {
        LogoutResponse result;
        try {
            String authToken = request.getAuthToken();
            database.getAuth(authToken);
            database.deleteAuth(authToken);
            result = new LogoutResponse(true, null);
        } catch(DataAccessException ex) {
            result = new LogoutResponse(false, "Error: " + ex.getMessage());
        }
        return result;
    }


    public String newAuthToken() {
        return UUID.randomUUID().toString();
    }

    public UserData getUser(String name) {
        try {
            return database.getUser(name);
        } catch(DataAccessException ex) {
            System.out.println("Caught DataAccessException, no user to return");
            return null;
        }
    }

    public void deleteUser(String name) {
        try {
            database.deleteUser(name);
        } catch(DataAccessException ex) {
            System.out.println("Caught DataAccessException, no user to delete");
        }
    }
}
