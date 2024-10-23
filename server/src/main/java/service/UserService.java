package service;

import dataaccess.DataAccessException;
import dataaccess.Database;
import models.*;
import request.*;
import result.*;
import request.LoginReq;
import request.RegisterReq;
import result.LoginRes;
import result.RegisterRes;

import java.util.UUID;

public class UserService {

    Database database;

    public UserService(Database database) {
        this.database = database;
    }
    public RegisterRes createUser(RegisterReq request) {
        String name = request.getUsername();
        String password = request.getPassword();
        String email = request.getEmail();
        RegisterRes result;
        try {
            database.createUser(name, password,email);
            String authToken = newAuthToken();
            database.createAuth(authToken, name);
            result = new RegisterRes(true, null, name, authToken);
        } catch(DataAccessException ex) {
            result = new RegisterRes(false, "Unable to create user", null, null);
        }
        return result;
    }
    public String newAuthToken() {
        return UUID.randomUUID().toString();
    }
    public LoginRes loginUser(LoginReq request) {
        String name = request.getUsername();
        String password = request.getPassword();
        LoginRes result;
        try {
            UserData user = database.getUser(name);
            if(user.password().equals(password)) {
                String newToken = newAuthToken();
                database.createAuth(newToken, name);
                result = new LoginRes(true, null, name, newToken);
            }
            else
                throw new DataAccessException("Invalid Password");
        } catch(DataAccessException ex) {
            result = new LoginRes(false, ex.getMessage(), null, null);
        }
        return result;
    }
    public LogoutRes logoutUser(LogoutReq request) {
        LogoutRes result;
        try {
            String authToken = request.getAuthToken();
            database.getAuth(authToken);
            database.deleteAuth(authToken);
            result = new LogoutRes(true, null);
        } catch(DataAccessException ex) {
            result = new LogoutRes(false, "Error: " + ex.getMessage());
        }
        return result;
    }

}