package handlers;
import dataaccess.*;
import request.*;
import result.*;
import service.*;
import spark.*;

public class ListGames implements Route {

    private Database database;
    private MethodHandlers methodHandlers;

    public ListGames(Database database) {
        this.database = database;
        this.methodHandlers = new MethodHandlers();
    }

    @Override
    public Object handle(Request request, Response response) {
        try {
            String token = methodHandlers.getAuthorization(request);
            methodHandlers.isNullString(token);
            database.getAuth(token);
            GameService listGames = new GameService(database);
            ListGamesRes listGamesResult = listGames.listGames(new ListGamesReq(token));
            int statusCode = listGamesResult.isSuccess() ? 200 : 500;
            return methodHandlers.getResponse(response, statusCode, listGamesResult);
        } catch (DataAccessException ex) {
            return methodHandlers.getResponse(response, 401, new LoginRes(null, "Error: unauthorized", null, null));
        } catch (Exception ex) {
            return methodHandlers.getResponse(response, 400, new JoinGameRes(null, "Error: bad request"));
        }
    }
}