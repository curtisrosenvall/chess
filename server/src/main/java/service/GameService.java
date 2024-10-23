package service;

import dataaccess.*;
import models.*;
import request.*;
import result.*;
import java.util.ArrayList;
public class GameService {

    Database database;

    public GameService(Database database) {
        this.database = database;
    }

    public CreateGameRes createGame(CreateGameRequest request) {
        String authToken = request.getAuthToken();
        String gameName = request.getGameName();
        CreateGameRes result;
        try {
            database.getAuth(authToken);
            if(!database.noGameName(gameName))
                throw new DataAccessException("Game already Exists");
            database.createGame(gameName);
            GameData game = database.getGameName(gameName);
            result = new CreateGameRes(true, null, game.gameID());
        } catch(DataAccessException ex) {
            result = new CreateGameRes(false, "Error: " + ex.getMessage(), null);
        }
        return result;
    }



}