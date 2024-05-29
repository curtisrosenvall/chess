package handler;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import request.CreateGameRequest;
import request.JoinGameRequest;
import spark.Request;
import spark.Response;
import request.RegisterRequest;
import request.LoginRequest;
import response.ParentResponse;

public class methodHandlers {

    public Object getBody(Request request, String requestType) throws DataAccessException {
        if(requestType.equals("RegisterRequest"))
            return new Gson().fromJson(request.body(), RegisterRequest.class);
        else if(requestType.equals("LoginRequest"))
            return new Gson().fromJson(request.body(), LoginRequest.class);
        else if(requestType.equals("CreateGameRequest"))
            return new Gson().fromJson(request.body(), CreateGameRequest.class);
        else if(requestType.equals("JoinGameRequest"))
            return new Gson().fromJson(request.body(), JoinGameRequest.class);
        else
            throw new DataAccessException("Bad Request");
    }

    public String getAuthorization(Request request){
        return request.headers("Authorization");
    }

    public String getResponse(Response response, int status, ParentResponse objectClass ) {
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
