package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;
import response.ParentResponse;

public class methodHandlers {

    public Object getBody(Request request, String requestType) throws DataAccessException {
        if(requestType.equals("RegisterRequest")){
            return new Gson().fromJson(request.body(), RegisterRequest.class);
        }else {
            throw new DataAccessException("Invalid request type");
        }
    }

    public String getAuth(Request request){
        return request.headers("Authorization");
    }

    public String getResponse(Response response, int status, ParentResponse objectClass ) throws DataAccessException {
        response.status(status);
        response.type("application/json");
        objectClass.nullSuccess();
        return new Gson().toJson(objectClass);
    }
}
