package handler;

import com.google.gson.Gson;
import dataaccess.Database;

import result.ClearResult;
import service.ClearGameService;

import spark.Request;
import spark.Response;
import spark.Route;

public class ClearAll implements Route {

    Database database;
    MethodHandlers methodHandlers;

    public ClearAll(Database database) {
        this.database = database;
        methodHandlers = new MethodHandlers();
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