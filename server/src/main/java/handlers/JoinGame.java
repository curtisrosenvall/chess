package handlers;

import dataaccess.DataAccessException;
import dataaccess.Database;
import models.GameData;
import request.JoinGameReq;
import result.JoinGameRes;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class JoinGame implements Route {

    private Database database;
    private MethodHandlers methodHandlers;

    public JoinGame(Database database) {
        this.database = database;
        this.methodHandlers = new MethodHandlers();
    }
    @Override
    public Object handle(Request request, Response response) {
        JoinGameReq joinRequest;
        String token;
        GameData game;
        try {
            joinRequest = (JoinGameReq) methodHandlers.getBody(request, "JoinGameRequest");
            methodHandlers.isNullString(joinRequest.getPlayerColor());
            methodHandlers.isNullInteger(joinRequest.getGameID());
            token = methodHandlers.getAuthorization(request);
            game = database.getGame(joinRequest.getGameID());
        } catch (DataAccessException ex) {
            return methodHandlers.getResponse(response, 400, new JoinGameRes(null, "Error: bad request"));
        }
        try {
            methodHandlers.isNullString(token);
            database.getAuth(token);
            joinRequest.setAuthToken(token);
        } catch (DataAccessException ex) {
            return methodHandlers.getResponse(response, 401, new JoinGameRes(null, "Error: unauthorized"));
        }
        try {
            String playerColor = joinRequest.getPlayerColor();
            if (playerColor.equalsIgnoreCase("WHITE")) {
                if (database.getPlayerFromColor(game, "WHITE") != null) {
                    throw new DataAccessException("Error: already taken");
                }
            } else if (playerColor.equalsIgnoreCase("BLACK")) {
                if (database.getPlayerFromColor(game, "BLACK") != null) {
                    throw new DataAccessException("Error: already taken");
                }
            }
        } catch (DataAccessException ex) {
            return methodHandlers.getResponse(response, 403, new JoinGameRes(null, "Error: already taken"));
        }
        GameService joinGame = new GameService(database);
        JoinGameRes joinGameResult = joinGame.joinGame(joinRequest);

        if (joinGameResult.isSuccess()) {
            return methodHandlers.getResponse(response, 200, joinGameResult);
        } else {
            return methodHandlers.getResponse(response, 500, joinGameResult);
        }
    }
}