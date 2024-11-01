package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import models.*;
import java.sql.*;
import java.util.ArrayList;

public class SQLGameDAO implements GameDAO {


    @Override
    public void clear() throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("TRUNCATE game");
            statement.executeUpdate();
        } catch(SQLException exception) {
            throw new DataAccessException("Error: " + exception.getMessage());
        }
    }

    @Override
    public void createGame(String name) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO game (gameName, game) VALUES (?,?)");
            statement.setString(1,name);
            String gameJson = new Gson().toJson(new ChessGame());
            statement.setString(2,gameJson);
            statement.executeUpdate();
        } catch(SQLException ex) {
            throw new DataAccessException("Error: " + ex.getMessage());
        }
    }
    @Override
    public GameData getGame(int id) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            PreparedStatement statement = conn.prepareStatement("SELECT whiteUsername, blackUsername, gameName, game FROM game WHERE gameID=?");
            statement.setInt(1, id);
            ResultSet queryResult = statement.executeQuery();
            if(queryResult.next()) {
                String whiteUsername = queryResult.getString("whiteUsername");
                String blackUsername = queryResult.getString("blackUsername");
                String gameName = queryResult.getString("gameName");
                String json = queryResult.getString("game");
                ChessGame game = new Gson().fromJson(json, ChessGame.class);
                return new GameData(id, whiteUsername, blackUsername, gameName, game);
            }
        } catch(SQLException ex) {
            throw new DataAccessException("Error: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        ArrayList<GameData> gameList = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName FROM game");
            try (ResultSet allGames = ps.executeQuery()) {
                while (allGames.next()) {
                    int gameID = allGames.getInt("gameID");
                    String whiteUsername = allGames.getString("whiteUsername");
                    String blackUsername = allGames.getString("blackUsername");
                    String gameName = allGames.getString("gameName");
                    gameList.add(new GameData(gameID, whiteUsername, blackUsername, gameName, null));
                }
                return gameList;
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error: " + ex.getMessage());
        }
    }

    @Override
    public void updateGame(int id, GameData game) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE game SET whiteUsername=?, blackUsername=?, gameName=?, game=? WHERE gameID=?");
            preparedStatement.setString(1,game.whiteUsername());
            preparedStatement.setString(2,game.blackUsername());
            preparedStatement.setString(3, game.gameName());
            String json = new Gson().toJson(game.game());
            preparedStatement.setString(4,json);
            preparedStatement.setInt(5,id);
            preparedStatement.executeUpdate();
        } catch(SQLException ex) {
            throw new DataAccessException("Error: " + ex.getMessage());
        }
    }

    @Override
    public int size(){
        try(Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("SELECT COUNT(*) FROM game");
            ResultSet queryResult = statement.executeQuery();
            if(queryResult.next()) {
                return queryResult.getInt(1);
            }
        } catch(SQLException | DataAccessException ex) {
            return -1;
        }
        return -1;
    }
}
