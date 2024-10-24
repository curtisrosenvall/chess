package handlers;
import dataaccess.DataAccessException;
import dataaccess.Database;
import models.UserData;
import request.*;
import result.*;
import service.UserService;
import spark.*;

public class Login implements Route {
    private Database database;
    private MethodHandlers methodHandlers;

    public Login(Database database) {
        this.database = database;
        this.methodHandlers = new MethodHandlers();
    }
    @Override
    public Object handle(Request request, Response response) {
        try {
            LoginReq loginRequest = (LoginReq) methodHandlers.getBody(request, "LoginReq");
            methodHandlers.isNullString(loginRequest.getUsername());
            methodHandlers.isNullString(loginRequest.getPassword());

            UserData user = database.getUser(loginRequest.getUsername());
            UserService loginService = new UserService(database);
            LoginRes loginResult = loginService.loginUser(loginRequest);

            if (user.password().equals(loginRequest.getPassword())) {
                int statusCode = loginResult.isSuccess() ? 200 : 500;
                return methodHandlers.getResponse(response, statusCode, loginResult);
            } else {
                throw new DataAccessException("Error: unauthorized");
            }
        } catch (DataAccessException ex) {
            return methodHandlers.getResponse(response, 401, new LoginRes(null, "Error: unauthorized", null, null));
        } catch (Exception ex) {
            return methodHandlers.getResponse(response, 401, new LoginRes(null, "Error: bad request", null, null));
        }
    }
}