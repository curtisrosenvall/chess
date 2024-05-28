package handler;
import dataaccess.DataAccessException;
import dataaccess.Database;
import request.LogoutRequest;
import response.LoginResponse;
import response.LogoutResponse;
import spark.Request;
import spark.Route;
import spark.Response;
import model.*;
import service.*;

public class Logout implements Route {

    Database database;
    methodHandlers methodHandlers;

    public Logout(Database database) {
        this.database = database;
        methodHandlers = new methodHandlers();

    }

    public Object handle(Request request, Response response) {
        LogoutRequest logoutRequest;
        String token;
        try {
            token = methodHandlers.getAuth(request);
            methodHandlers.isNullString(token);
            logoutRequest = new LogoutRequest(token);
        } catch(DataAccessException ex) {
            return methodHandlers.getResponse(response,400, new LogoutResponse(null, "Error: bad request"));
        }
        try {
            AuthData auth = database.getAuth(token);
            UserService logout = new UserService(database);
            LogoutResponse logoutResponse = logout.logoutUser(logoutRequest);
            if(logoutResponse.isSuccess())
                return methodHandlers.getResponse(response, 200, logoutResponse);
            else
                return methodHandlers.getResponse(response, 500, logoutResponse);
        } catch(DataAccessException ex) {
            return methodHandlers.getResponse(response, 401, new LoginResponse(null, "Error: unauthorized", null, null));
        }
    }
}
