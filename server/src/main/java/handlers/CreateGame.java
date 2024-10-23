package handlers;

import dataaccess.DataAccessException;
import dataaccess.Database;
import request.*;
import result.*;
import service.GameService;
import spark.*;

public class CreateGame implements Route {

    Database database;
    MethodHandlers MethodHandlers;

    public CreateGame(Database database) {
        this.database = database;
        MethodHandlers = new MethodHandlers();
    }

    @Override
    public Object handle(Request request, Response response) {
        CreateGameRequest createRequest;
        String token;
        try {
            createRequest = (CreateGameRequest) MethodHandlers.getBody(request, "CreateGameRequest");
            MethodHandlers.isNullString(createRequest.getGameName());
            token = MethodHandlers.getAuthorization(request);
        } catch(DataAccessException ex) {
            return MethodHandlers.getResponse(response,400, new CreateGameRes(null, "Error: bad request", null));
        }
        try {
            MethodHandlers.isNullString(token);
            database.getAuth(token);
            createRequest.setAuthToken(token);
        } catch(DataAccessException ex) {
            return MethodHandlers.getResponse(response, 401, new CreateGameRes(null, "Error: unauthorized",null));
        }
        GameService createGame = new GameService(database);
        CreateGameRes createGameResult = createGame.createGame(createRequest);
        if(createGameResult.isSuccess()) {
            return MethodHandlers.getResponse(response, 200, createGameResult);
        } else {
            return MethodHandlers.getResponse(response, 500, createGameResult);
        }
    }
}