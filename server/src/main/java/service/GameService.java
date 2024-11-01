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

    public JoinGameRes joinGame(JoinGameReq request) {
        String authToken = request.getAuthToken();
        JoinGameRes result;
        try {
            AuthData auth = database.getAuth(authToken);
            GameData game = database.getGame(request.getGameID());
            GameData newGame;
            if(request.getPlayerColor().equalsIgnoreCase("WHITE")) {
                if(database.getPlayerFromColor(game, "WHITE") != null)
                    throw new DataAccessException("Error: already taken");
                newGame = new GameData(game.gameID(), auth.username(), game.blackUsername(), game.gameName(), game.game());
                database.updateGame(newGame);
            } else if (request.getPlayerColor().equalsIgnoreCase("BLACK")) {
                if(database.getPlayerFromColor(game, "BLACK") != null)
                    throw new DataAccessException("Error: already taken");
                newGame = new GameData(game.gameID(), game.whiteUsername(), auth.username(), game.gameName(), game.game());
                database.updateGame(newGame);
            }
            result = new JoinGameRes(true, null);
        } catch(DataAccessException ex) {
            result = new JoinGameRes(false, ex.getMessage());
        }
        return result;
    }

    public ListGamesRes listGames(ListGamesReq request) {
        String authToken = request.getAuthToken();
        ListGamesRes result;
        try {
            database.getAuth(authToken);
            ArrayList<GameData> games = database.getGameList();
            result = new ListGamesRes (true, null, games);
        } catch(DataAccessException ex) {
            result = new ListGamesRes(false, ex.getMessage(), null);
        }
        return result;
    }
}