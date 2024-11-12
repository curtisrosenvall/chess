package handlers;

import com.google.gson.Gson;
import dataaccess.Database;
import result.ClearRes;
import service.ClearGameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class Clear implements Route {
    private final Database database;
    private final MethodHandlers methodHandlers;
    public Clear(Database database) {
        this.database = database;
        this.methodHandlers = new MethodHandlers();
    }
    @Override
    public Object handle(Request request, Response response) {
        ClearGameService clearService = new ClearGameService(database);
        ClearRes clearResult = clearService.deleteAll();

        int statusCode = clearResult.isSuccess() ? 200 : 500;
        methodHandlers.getResponse(response, statusCode, clearResult);

        return new Gson().toJson(clearResult);
    }
}