package handlers;

import dataaccess.DataAccessException;
import dataaccess.Database;
import models.GameData;
import request.*;
import result.*;
import service.GameService;
import spark.*;

public class JoinGame implements Route {

    // Instance of the database
    Database database;
    // Instance of method handlers for processing requests
    MethodHandlers methodHandlers;

    // Constructor to initialize the database and method handlers
    public JoinGame(Database database) {
        this.database = database;
        methodHandlers = new MethodHandlers();
    }

    // Handle method to process the incoming request
    @Override
    public Object handle(Request request, Response response) {
        JoinGameReq joinRequest;
        String token;
        GameData game;
        try {
            // Parse the request body to create a JoinGameRequest object
            joinRequest = (JoinGameReq) methodHandlers.getBody(request, "JoinGameRequest");
            // Check if the player color and game ID are null or invalid
            methodHandlers.isNullString(joinRequest.getPlayerColor());
            methodHandlers.isNullInteger(joinRequest.getGameID());
            // Get the authorization token from the request
            token = methodHandlers.getAuthorization(request);
            // Retrieve the game data for the given game ID
            game = database.getGame(joinRequest.getGameID());
        } catch (DataAccessException ex) {
            // Return a 400 error response if there was an issue with the request data
            return methodHandlers.getResponse(response, 400, new JoinGameRes(null, "Error: bad request"));
        }
        try {
            // Check if the token is null or empty
            methodHandlers.isNullString(token);
            // Validate the token by checking it in the database
            database.getAuth(token);
            // Set the token in the joinRequest object
            joinRequest.setAuthToken(token);
        } catch (DataAccessException ex) {
            // Return a 401 error response if the token is invalid or unauthorized
            return methodHandlers.getResponse(response, 401, new JoinGameRes(null, "Error: unauthorized"));
        }
        try {
            String playerColor = joinRequest.getPlayerColor();
            // Check if the chosen player color is already taken in the game
            if (playerColor.equalsIgnoreCase("WHITE")) {
                if (database.getPlayerFromColor(game, "WHITE") != null)
                    throw new DataAccessException("Error: already taken");
            } else if (playerColor.equalsIgnoreCase("BLACK")) {
                if (database.getPlayerFromColor(game, "BLACK") != null)
                    throw new DataAccessException("Error: already taken");
            }
        } catch (DataAccessException ex) {
            // Return a 403 error response if the chosen player color is already taken
            return methodHandlers.getResponse(response, 403, new JoinGameRes(null, "Error: already taken"));
        }
        // Create a new GameService instance to handle joining the game
        GameService joinGame = new GameService(database);
        // Call the joinGame method and get the result
        JoinGameRes joinGameResult = joinGame.joinGame(joinRequest);
        if (joinGameResult.isSuccess()) {
            // Return a 200 response if the player joined the game successfully
            return methodHandlers.getResponse(response, 200, joinGameResult);
        } else {
            // Return a 500 error response if there was an issue joining the game
            return methodHandlers.getResponse(response, 500, joinGameResult);
        }
    }
}