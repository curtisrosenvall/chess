package service;
import dataaccess.*;
import model.*;
import request.*;
import response.*;

public class GameService {

    Database database;

    public GameService(Database database) {
        this.database = database;
    }

    public CreateGameResponse createGame(CreateGameRequest request) {
        String authToken = request.getAuthToken();
        String gameName = request.getGameName();
        CreateGameResponse result;
        try {
            database.getAuth(authToken);
            if(!database.noGameName(gameName))
                throw new DataAccessException("Game already Exists");
            database.createGame(gameName);
            GameData game = database.getGameName(gameName);
            result = new CreateGameResponse(true, null, game.gameId());
        } catch(DataAccessException ex) {
            result = new CreateGameResponse(false, "Error: " + ex.getMessage(), null);
        }
        return result;
    }

}
