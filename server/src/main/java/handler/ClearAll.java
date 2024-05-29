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
        ClearResult clearResult;

        try {
            clearResult = clear.deleteAll();
            if (clearResult.isSuccess()) {
                return methodHandlers.getResponse(response, 200, clearResult);
            } else {
                return methodHandlers.getResponse(response, 500, clearResult);
            }
        } catch (Exception ex) {
            // Catch any other unexpected exceptions
            System.err.println("Internal server error: " + ex.getMessage());  // Debug print
            return methodHandlers.getResponse(response, 500, new ClearResult(false, "Error: internal server error"));
        }
    }

    // Define custom exception for unauthorized access
    private class UnauthorizedAccessException extends Exception {
        public UnauthorizedAccessException(String message) {
            super(message);
        }
    }
}