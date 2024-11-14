package handlers;

import dataaccess.DataAccessException;
import dataaccess.Database;
import request.CreateGameReq;
import result.CreateGameRes;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateGame implements Route {

    private Database database;
    private MethodHandlers methodHandlers;

    public CreateGame(Database database) {
        this.database = database;
        this.methodHandlers = new MethodHandlers();
    }

    @Override
    public Object handle(Request request, Response response) {
        CreateGameReq createRequest;
        String token;
        try {
            createRequest = (CreateGameReq) methodHandlers.getBody(request, "CreateGameRequest");
            methodHandlers.isNullString(createRequest.getGameName());
            token = methodHandlers.getAuthorization(request);
        } catch (DataAccessException ex) {
            return methodHandlers.getResponse(response, 400, new CreateGameRes(null, "Error: bad request", null));
        }
        try {
            methodHandlers.isNullString(token);
            database.getAuth(token);
            createRequest.setAuthToken(token);
        } catch (DataAccessException ex) {
            return methodHandlers.getResponse(response, 401, new CreateGameRes(null, "Error: unauthorized", null));
        }
        GameService createGame = new GameService(database);
        CreateGameRes createGameResult = createGame.createGame(createRequest);
        if (createGameResult.isSuccess()) {
            return methodHandlers.getResponse(response, 200, createGameResult);
        } else {
            return methodHandlers.getResponse(response, 500, createGameResult);
        }
    }
}