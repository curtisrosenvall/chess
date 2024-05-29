package handler;
import result.ClearResult;
import service.ClearGameService;
import spark.Request;
import spark.Route;
import spark.Response;
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
        ClearResult clearResult = clear.deleteAll();
        if(clearResult.isSuccess()) {
            methodHandlers.getResponse(response, 200, clearResult);
        } else {
            methodHandlers.getResponse(response, 500, clearResult);
        }
        return new Gson().toJson(clearResult);
    }
}
