package service;

import dataaccess.*;
import model.*;
import request.*;
import result.*;

import java.util.UUID;

public class UserService {

    Database dataBase;

    public UserService(Database dataBase) {
        this.dataBase = dataBase;
    }

    public RegisterResult createUser(RegisterRequest request) {
        String name = request.getUsername();
        String password = request.getPassword();
        String email = request.getEmail();
        RegisterResult result;
        try {
            dataBase.createUser(name, password,email);
            String authToken = newAuthToken();
            dataBase.createAuth(authToken, name);
            result = new RegisterResult(true, null, name, authToken);
        } catch(DataAccessException ex) {
            result = new RegisterResult(false, ex.getMessage(), null, null);
        }
        return result;
    }

    public LoginResult loginUser(LoginRequest request) {
        String name = request.getUsername();
        String password = request.getPassword();
        LoginResult result;
        try {
            UserData user = dataBase.getUser(name);
            if(user.password().equals(password)) {
                String newToken = newAuthToken();
                dataBase.createAuth(newToken, name);
                result = new LoginResult(true, null, name, newToken);
            }
            else
                throw new DataAccessException("Invalid Password");
        } catch(DataAccessException ex) {
            result = new LoginResult(false, ex.getMessage(), null, null);
        }
        return result;
    }

    public LogoutResult logoutUser(LogoutRequest request) {
        LogoutResult result;
        try {
            String authToken = request.getAuthToken();
            dataBase.getAuth(authToken);
            dataBase.deleteAuth(authToken);
            result = new LogoutResult(true, null);
        } catch(DataAccessException ex) {
            result = new LogoutResult(false, "Error: " + ex.getMessage());
        }
        return result;
    }

    public String newAuthToken() {
        return UUID.randomUUID().toString();
    }
}