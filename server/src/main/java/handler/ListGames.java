package handler;
import dataaccess.DataAccessException;
import request.ListGamesRequest;
import result.JoinGameResponse;
import result.ListGamesResponse;
import result.LoginResult;
import spark.Request;
import spark.Route;
import spark.Response;
import dataaccess.Database;
import model.AuthData;
import service.*;

public class ListGames implements Route {
    Database database;
    methodHandlers methodHandlers;

    public ListGames(Database database) {
        this.database = database;
        methodHandlers = new methodHandlers();
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
            return methodHandlers.getResponse(response,400, new JoinGameResponse(null, "Error: bad request"));
        }
        try {
            AuthData auth = database.getAuth(token);
            GameService listGames = new GameService(database);
            ListGamesResponse listGamesResult = listGames.listGames(listRequest);
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
