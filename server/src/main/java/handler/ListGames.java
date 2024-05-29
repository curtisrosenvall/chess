package handler;

import dataaccess.DataAccessException;
import dataaccess.Database;
import request.ListGamesRequest;
import result.JoinGameResult;
import result.ListGamesResult;
import result.LoginResult;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ListGames implements Route {

    Database database;
    MethodHandlers methodHandlers;

    public ListGames(Database database) {
        this.database = database;
        methodHandlers = new MethodHandlers();
    }

    @Override
    public Object handle(Request request, Response response) {
        ListGamesRequest listRequest;
        String token;
        try {
            token = methodHandlers.getAuthorization(request);
            methodHandlers.isNullString(token);
            listRequest = new ListGamesRequest(token);
        } catch(DataAccessException ex) {
            return methodHandlers.getResponse(response,400, new JoinGameResult(null, "Error: bad request"));
        }
        try {
            database.getAuth(token);
            GameService listGames = new GameService(database);
            ListGamesResult listGamesResult = listGames.listGames(listRequest);
            if(listGamesResult.isSuccess()) {
                return methodHandlers.getResponse(response, 200, listGamesResult);
            } else {
                return methodHandlers.getResponse(response, 500, listGamesResult);
            }
        } catch(DataAccessException ex) {
            return methodHandlers.getResponse(response, 401, new LoginResult(null, "Error: unauthorized", null, null));
        }
    }
}