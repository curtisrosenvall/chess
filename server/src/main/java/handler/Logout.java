package handler;

import dataaccess.DataAccessException;
import dataaccess.Database;
import request.LogoutRequest;
import result.LoginResult;
import result.LogoutResult;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class Logout implements Route {

    Database database;
    MethodHandlers methodHandlers;

    public Logout(Database database) {
        this.database = database;
        methodHandlers = new MethodHandlers();
    }

    @Override
    public Object handle(Request request, Response response) {
        LogoutRequest logoutRequest;
        String token;
        try {
            token = methodHandlers.getAuthorization(request);
            methodHandlers.isNullString(token);
            logoutRequest = new LogoutRequest(token);
        } catch(DataAccessException ex) {
            return methodHandlers.getResponse(response,400, new LogoutResult(null, "Error: bad request"));
        }
        try {
            database.getAuth(token);
            UserService logout = new UserService(database);
            LogoutResult logoutResult = logout.logoutUser(logoutRequest);
            if(logoutResult.isSuccess())
                return methodHandlers.getResponse(response, 200, logoutResult);
            else
                return methodHandlers.getResponse(response, 500, logoutResult);
        } catch(DataAccessException ex) {
            return methodHandlers.getResponse(response, 401, new LogoutResult(null, "Error: unauthorized"));
        }
    }
}
