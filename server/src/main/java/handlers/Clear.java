package handlers;

import com.google.gson.Gson;
import dataaccess.Database;
import result.*;
import service.ClearGameService;
import spark.*;

public class Clear implements Route {

    // Instance of the database
    Database database;
    // Instance of method handlers for processing requests
    MethodHandlers methodHandlers;

    // Constructor to initialize the database and method handlers
    public Clear(Database database) {
        this.database = database;
        methodHandlers = new MethodHandlers();
    }

    // Handle method to process the incoming request
    @Override
    public Object handle(Request request, Response response) {
        // Create a new ClearGameService instance to handle clearing all data
        ClearGameService clear = new ClearGameService(database);
        // Call the deleteAll method and get the result
        ClearRes clearResult = clear.deleteAll();
        if (clearResult.isSuccess()) {
            // Return a 200 response if clearing the data was successful
            methodHandlers.getResponse(response, 200, clearResult);
        } else {
            // Return a 500 error response if there was an issue clearing the data
            methodHandlers.getResponse(response, 500, clearResult);
        }
        // Return the result as a JSON object
        return new Gson().toJson(clearResult);
    }
}