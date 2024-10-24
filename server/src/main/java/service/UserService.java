package service;

import dataaccess.*;
import models.*;
import request.*;
import result.*;

import java.util.UUID;

public class UserService {

    Database dataBase;

    public UserService(Database dataBase) {
        this.dataBase = dataBase;
    }

    public RegisterRes createUser(RegisterReq request) {
        String name = request.getUsername();
        String password = request.getPassword();
        String email = request.getEmail();
        RegisterRes result;
        try {
            dataBase.createUser(name, password,email);
            String authToken = newAuthToken();
            dataBase.createAuth(authToken, name);
            result = new RegisterRes(true, null, name, authToken);
        } catch(DataAccessException ex) {
            result = new RegisterRes(false, ex.getMessage(), null, null);
        }
        return result;
    }

    public LoginRes loginUser(LoginReq request) {
        String name = request.getUsername();
        String password = request.getPassword();
        LoginRes result;
        try {
            UserData user = dataBase.getUser(name);
            if(user.password().equals(password)) {
                String newToken = newAuthToken();
                dataBase.createAuth(newToken, name);
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
            dataBase.getAuth(authToken);
            dataBase.deleteAuth(authToken);
            result = new LogoutRes(true, null);
        } catch(DataAccessException ex) {
            result = new LogoutRes(false, "Error: " + ex.getMessage());
        }
        return result;
    }

    public String newAuthToken() {
        return UUID.randomUUID().toString();
    }
}