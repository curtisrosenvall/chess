package service;
import dataaccess.*;
import model.*;
import request.*;
import result.*;
import java.util.ArrayList;
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

    public JoinGameResponse joinGame(JoinGameRequest request) {
        String authToken = request.getAuthToken();
        JoinGameResponse result;
        try {
            AuthData auth = database.getAuth(authToken);
            GameData game = database.getGame(request.getGameId());
            GameData newGame;
            if(request.getPlayerColor().equalsIgnoreCase("WHITE")) {
                if(database.getPlayerFromColor(game, "WHITE") != null)
                    throw new DataAccessException("Error: already taken");
                newGame = new GameData(game.gameId(), auth.username(), game.blackUsername(), game.gameName(), game.game());
                database.updateGame(newGame);
            } else if (request.getPlayerColor().equalsIgnoreCase("BLACK")) {
                if(database.getPlayerFromColor(game, "BLACK") != null)
                    throw new DataAccessException("Error: already taken");
                newGame = new GameData(game.gameId(), game.whiteUsername(), auth.username(), game.gameName(), game.game());
                database.updateGame(newGame);
            }
            result = new JoinGameResponse(true, null);
        } catch(DataAccessException ex) {
            result = new JoinGameResponse(false, ex.getMessage());
        }
        return result;
    }

    public ListGamesResponse listGames(ListGamesRequest request) {
        String authToken = request.getAuthToken();
        ListGamesResponse result;
        try {
            database.getAuth(authToken);
            ArrayList<GameData> games = database.getGameList();
            result = new ListGamesResponse (true, null, games);
        } catch(DataAccessException ex) {
            result = new ListGamesResponse(false, ex.getMessage(), null);
        }
        return result;
    }

}
