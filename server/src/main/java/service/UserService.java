package service;

import dataaccess.DataAccessException;
import dataaccess.Database;
import request.RegisterRequest;
import model.*;
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
