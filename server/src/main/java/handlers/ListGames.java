package handlers;
import dataaccess.*;
import request.*;
import result.*;
import service.GameService;
import spark.*;


public class ListGames implements Route {

    // Instance of the database
    Database database;
    // Instance of method handlers for processing requests
    MethodHandlers methodHandlers;

    // Constructor to initialize the database and method handlers
    public ListGames(Database database) {
        this.database = database;
        methodHandlers = new MethodHandlers();
    }

    // Handle method to process the incoming request
    @Override
    public Object handle(Request request, Response response) {
        ListGamesReq listRequest;
        String token;
        try {
            // Get the authorization token from the request
            token = methodHandlers.getAuthorization(request);
            // Check if the token is null or empty
            methodHandlers.isNullString(token);
            // Create a new ListGamesReq object with the token
            listRequest = new ListGamesReq(token);
        } catch (DataAccessException ex) {
            // Return a 400 error response if there was an issue with the request data
            return methodHandlers.getResponse(response, 400, new JoinGameRes(null, "Error: bad request"));
        }
        try {
            // Validate the token by checking it in the database
            database.getAuth(token);
            // Create a new GameService instance to handle listing games
            GameService listGames = new GameService(database);
            // Call the listGames method and get the result
            ListGamesRes listGamesResult = listGames.listGames(listRequest);
            if (listGamesResult.isSuccess()) {
                // Return a 200 response if the games were listed successfully
                return methodHandlers.getResponse(response, 200, listGamesResult);
            } else {
                // Return a 500 error response if there was an issue listing the games
                return methodHandlers.getResponse(response, 500, listGamesResult);
            }
        } catch (DataAccessException ex) {
            // Return a 401 error response if the token is invalid or unauthorized
            return methodHandlers.getResponse(response, 401, new LoginRes(null, "Error: unauthorized", null, null));
        }
    }
}