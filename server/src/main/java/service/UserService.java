package service;


import dataaccess.*;
import model.*;
import request.*;
import response.*;

import java.util.UUID;

public class UserService {

    Database dataBase;

    public UserService(Database dataBase) {
        this.dataBase = dataBase;
    }

    public RegisterResponse createUser(RegisterRequest request) {
        String name = request.getUsername();
        String password = request.getPassword();
        String email = request.getEmail();
        RegisterResponse result;
        try {
            dataBase.createUser(name, password,email);
            String authToken = newAuthToken();
            dataBase.createAuth(authToken, name);
            result = new RegisterResponse(true, null, name, authToken);
        } catch(DataAccessException ex) {
            result = new RegisterResponse(false, ex.getMessage(), null, null);
        }
        return result;
    }

    public LoginResponse loginUser(LoginRequest request) {
        String name = request.getUsername();
        String password = request.getPassword();
        LoginResponse result;
        try {
            UserData user = dataBase.getUser(name);
            if(user.password().equals(password)) {
                String newToken = newAuthToken();
                dataBase.createAuth(newToken, name);
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
            dataBase.getAuth(authToken);
            dataBase.deleteAuth(authToken);
            result = new LogoutResponse(true, null);
        } catch(DataAccessException ex) {
            result = new LogoutResponse(false, "Error: " + ex.getMessage());
        }
        return result;
    }

    public String newAuthToken() {
        return UUID.randomUUID().toString();
    }
}