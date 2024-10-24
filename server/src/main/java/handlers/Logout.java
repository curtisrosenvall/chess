package handlers;
import dataaccess.*;
import request.*;
import result.*;
import service.UserService;
import spark.*;

public class Logout implements Route {
    Database database;
    MethodHandlers methodHandlers;

    public Logout(Database database) {
        this.database = database;
        methodHandlers = new MethodHandlers();
    }

    @Override
    public Object handle(Request request, Response response) {
        try {
            String token = methodHandlers.getAuthorization(request);
            methodHandlers.isNullString(token);
            LogoutReq logoutRequest = new LogoutReq(token);
            database.getAuth(token);
            LogoutRes logoutResult = new UserService(database).logoutUser(logoutRequest);
            int statusCode = logoutResult.isSuccess() ? 200 : 500;
            return methodHandlers.getResponse(response, statusCode, logoutResult);
        } catch (DataAccessException ex) {
            return methodHandlers.getResponse(response, 401, new LogoutRes(null, "Error: unauthorized"));
        } catch (Exception ex) {
            return methodHandlers.getResponse(response, 400, new LogoutRes(null, "Error: bad request"));
        }
    }
}