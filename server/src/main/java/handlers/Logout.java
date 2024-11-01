package handlers;
import dataaccess.DataAccessException;
import dataaccess.Database;
import request.*;
import result.*;
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
        LogoutReq logoutRequest;
        String token;
        try {
            token = methodHandlers.getAuthorization(request);
            methodHandlers.isNullString(token);
            logoutRequest = new LogoutReq(token);
        } catch(DataAccessException ex) {
            return methodHandlers.getResponse(response,400, new LogoutRes(null, "Error: bad request"));
        }
        try {
            database.getAuth(token);
            UserService logout = new UserService(database);
            LogoutRes logoutResult = logout.logoutUser(logoutRequest);
            if(logoutResult.isSuccess())
                return methodHandlers.getResponse(response, 200, logoutResult);
            else
                return methodHandlers.getResponse(response, 500, logoutResult);
        } catch(DataAccessException ex) {
            return methodHandlers.getResponse(response, 401, new LogoutRes(null, "Error: unauthorized"));
        }
    }
}