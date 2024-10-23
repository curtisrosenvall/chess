package handlers;

import com.google.gson.Gson;
import dataaccess.Database;

import result.ClearRes;
import service.ClearGameService;

import spark.Request;
import spark.Response;
import spark.Route;

public class Clear implements Route {

    Database database;
    MethodHandlers MethodHandlers;

    public Clear(Database database) {
        this.database = database;
        MethodHandlers = new MethodHandlers();
    }

    @Override
    public Object handle(Request request, Response response) {
        ClearGameService clear = new ClearGameService(database);
        ClearRes clearResult = clear.deleteAll();
        if(clearResult.isSuccess()) {
            MethodHandlers.getResponse(response, 200, clearResult);
        } else {
            MethodHandlers.getResponse(response, 500, clearResult);
        }
        return new Gson().toJson(clearResult);
    }
}