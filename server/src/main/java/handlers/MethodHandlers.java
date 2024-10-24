package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import request.*;
import result.*;
import spark.Request;
import spark.Response;

public class MethodHandlers {

    public Object getBody(Request request, String requestType) throws DataAccessException {
        switch (requestType) {
            case "RegisterReq":
                return new Gson().fromJson(request.body(), RegisterReq.class);
            case "LoginReq":
                return new Gson().fromJson(request.body(), LoginReq.class);
            case "JoinGameRequest":
                return new Gson().fromJson(request.body(), JoinGameReq.class);
            case "CreateGameRequest":
                return new Gson().fromJson(request.body(), CreateGameRequest.class);
            default:
                throw new DataAccessException("Bad Request");
        }
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
        if (variable == null)
            throw new DataAccessException("Error: bad request");
        return true;
    }

    public boolean isNullInteger(Integer num) throws DataAccessException {
        if (num == null)
            throw new DataAccessException("Error: bad request");
        return true;
    }
}