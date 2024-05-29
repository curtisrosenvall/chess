package handler;
import dataaccess.DataAccessException;
import result.CreateGameResponse;
import spark.Request;
import spark.Response;
import service.*;
import dataaccess.Database;
import request.CreateGameRequest;
import spark.Route;

public class CreateGame implements Route {

    Database database;
    methodHandlers methodHandlers;

    public CreateGame(Database database) {
        this.database = database;
        methodHandlers = new methodHandlers();
    }

    @Override
    public Object handle(Request request, Response response) {
        CreateGameRequest createRequest;
        String token;
        try {
            createRequest = (CreateGameRequest) methodHandlers.getBody(request, "CreateGameRequest");
            methodHandlers.isNullString(createRequest.getGameName());
            token = methodHandlers.getAuthorization(request);
        } catch(DataAccessException ex) {
            return methodHandlers.getResponse(response,400, new CreateGameResponse(null, "Error: bad request", null));
        }
        try {
            methodHandlers.isNullString(token);
            database.getAuth(token);
            createRequest.setAuthToken(token);
        } catch(DataAccessException ex) {
            return methodHandlers.getResponse(response, 401, new CreateGameResponse(null, "Error: unauthorized",null));
        }
        GameService createGame = new GameService(database);
        CreateGameResponse createGameResult = createGame.createGame(createRequest);
        if(createGameResult.isSuccess()) {
            return methodHandlers.getResponse(response, 200, createGameResult);
        } else {
            return methodHandlers.getResponse(response, 500, createGameResult);
        }
    }
}
