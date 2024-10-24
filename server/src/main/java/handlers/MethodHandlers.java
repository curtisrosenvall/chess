package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import request.*;
import result.ParentRes;
import spark.Request;
import spark.Response;

public class MethodHandlers {

    public MethodHandlers() {

    }

    public Object getBody(Request request, String requestType) throws DataAccessException {
        if(requestType.equals("RegisterRequest"))
            return new Gson().fromJson(request.body(), RegisterReq.class);
        else if(requestType.equals("LoginRequest"))
            return new Gson().fromJson(request.body(), LoginReq.class);
        else if(requestType.equals("JoinGameRequest"))
            return new Gson().fromJson(request.body(), JoinGameReq.class);
        else if(requestType.equals("CreateGameRequest"))
            return new Gson().fromJson(request.body(), CreateGameRequest.class);
        else  //Clear, Logout, ListGames don't have bodies
            throw new DataAccessException("Bad Request");
    }

    public String getAuthorization(Request request) {
        return request.headers("Authorization");
    }

    public String getResponse(Response response, int status, ParentRes objectClass) {
        response.status(status);
        response.type("application/json");
        objectClass.nullSuccess();
        return new Gson().toJson(objectClass);
    }

    public boolean isNullString(String variable) throws DataAccessException {
        if(variable == null)
            throw new DataAccessException("Error: bad request");
        return true;
    }

    public boolean isNullInteger(Integer num) throws DataAccessException {
        if(num == null)
            throw new DataAccessException("Error: bad request");
        return true;
    }
}