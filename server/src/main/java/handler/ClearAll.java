package handler;
import service.ClearGameService;
import spark.Request;
import spark.Route;
import spark.Response;
import result.ClearResponse;
import com.google.gson.Gson;



import dataaccess.Database;

public class ClearAll implements Route {

    Database database;
    methodHandlers methodHandlers;

    public ClearAll(Database database) {
        this.database = database;
        methodHandlers = new methodHandlers();
    }

    @Override
    public Object handle(Request request, Response response) {
        ClearGameService clear = new ClearGameService(database);
        ClearResponse ClearResponse = clear.deleteAll();
        if(ClearResponse.isSuccess()) {
            methodHandlers.getResponse(response, 200, ClearResponse);
        } else {
            methodHandlers.getResponse(response, 500, ClearResponse);
        }
            return new Gson().toJson(ClearResponse);
    }
}
