package handler;
import dataaccess.DataAccessException;
import dataaccess.Database;

import spark.Request;
import model.GameData;
import service.GameService;
import request.*;
import result.*;
import spark.Response;
import spark.Route;

public class JoinGame implements Route{

    Database database;
    methodHandlers methodHandlers;

    public JoinGame(Database database) {
        this.database = database;
        methodHandlers = new methodHandlers();
    }

    @Override
    public Object handle(Request request, Response response) {
        JoinGameRequest joinRequest;
        String token;
        GameData game;
        try {
            joinRequest = (JoinGameRequest) methodHandlers.getBody(request, "JoinGameRequest");
            methodHandlers.isNullString(joinRequest.getPlayerColor());
            methodHandlers.isNullInteger(joinRequest.getGameId());
            token = methodHandlers.getAuthorization(request);
            game = database.getGame(joinRequest.getGameId());
        } catch(DataAccessException ex) {
            return methodHandlers.getResponse(response,400, new JoinGameResponse(null, "Error: bad request"));
        }
        try {
            methodHandlers.isNullString(token);
            database.getAuth(token);
            joinRequest.setAuthToken(token);
            database.getAuth(token);
        } catch(DataAccessException ex) {
            return methodHandlers.getResponse(response, 401, new JoinGameResponse(null, "Error: unauthorized"));
        }
        try {
            String playerColor = joinRequest.getPlayerColor();
            if(playerColor.equalsIgnoreCase("WHITE")) {
                if(database.getPlayerFromColor(game, "WHITE") != null)
                    throw new DataAccessException("Error: already taken");
            } else if(playerColor.equalsIgnoreCase("BLACK")) {
                if(database.getPlayerFromColor(game, "BLACK") != null)
                    throw new DataAccessException("Error: already taken");
            }
        } catch(DataAccessException ex) {
            return methodHandlers.getResponse(response, 403, new JoinGameResponse(null, "Error: already taken"));
        }
        GameService joinGame = new GameService(database);
        JoinGameResponse joinGameResult = joinGame.joinGame(joinRequest);
        if(joinGameResult.isSuccess()) {
            return methodHandlers.getResponse(response, 200, joinGameResult);
        } else {
            return methodHandlers.getResponse(response, 500, joinGameResult);
        }
    }
}
