package handlers;

import dataaccess.DataAccessException;
import dataaccess.Database;
import request.*;
import result.*;
import service.GameService;
import spark.*;

public class CreateGame implements Route {

    // Instance of the database
    Database database;
    // Instance of method handlers for processing requests
    MethodHandlers methodHandlers;

    // Constructor to initialize database and method handlers
    public CreateGame(Database database) {
        this.database = database;
        methodHandlers = new MethodHandlers();
    }

    // Handle method to process the incoming request
    @Override
    public Object handle(Request request, Response response) {
        CreateGameRequest createRequest;
        String token;
        try {
            // Parse the request body to create a CreateGameRequest object
            createRequest = (CreateGameRequest) methodHandlers.getBody(request, "CreateGameRequest");
            // Check if the game name is null or empty
            methodHandlers.isNullString(createRequest.getGameName());
            // Get the authorization token from the request
            token = methodHandlers.getAuthorization(request);
        } catch (DataAccessException ex) {
            // Return a 400 error response if there was an issue with the request data
            return methodHandlers.getResponse(response, 400, new CreateGameRes(null, "Error: bad request", null));
        }
        try {
            // Check if the token is null or empty
            methodHandlers.isNullString(token);
            // Validate the token by checking it in the database
            database.getAuth(token);
            // Set the token in the createRequest object
            createRequest.setAuthToken(token);
        } catch (DataAccessException ex) {
            // Return a 401 error response if the token is invalid or unauthorized
            return methodHandlers.getResponse(response, 401, new CreateGameRes(null, "Error: unauthorized", null));
        }
        // Create a new GameService instance to handle game creation
        GameService createGame = new GameService(database);
        // Call the createGame method and get the result
        CreateGameRes createGameResult = createGame.createGame(createRequest);
        if (createGameResult.isSuccess()) {
            // Return a 200 response if the game was created successfully
            return methodHandlers.getResponse(response, 200, createGameResult);
        } else {
            // Return a 500 error response if there was an issue creating the game
            return methodHandlers.getResponse(response, 500, createGameResult);
        }
    }
}