package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLGameDAO implements GameDAO {

    @Override
    public void clear() throws DataAccessException {
        try(Connection connection = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("TRUNCATE game");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }

    @Override
    public void createGame(String name) throws DataAccessException {
        try(Connection connection = DatabaseManager.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO  game (gameName,game) VALUES (?,?)");
            preparedStatement.setString(1,name);
            String gameJson = new Gson().toJson(new ChessGame());
            preparedStatement.setString(2,gameJson);
            preparedStatement.executeUpdate();
        }catch (SQLException x) {
            throw new DataAccessException("Error: " + x.getMessage());
        }
    }

    @Override
    public void updateGame(int id, GameData game) throws DataAccessException {
        try(Connection connection = DatabaseManager.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE  game SET whiteUsername=?, blackUsername=?, gameName=?, game=? WHERE gameID=?");
            preparedStatement.setString(1,game.whiteUsername());
            preparedStatement.setString(1,game.blackUsername());
            preparedStatement.setString(1,game.gameName());
            String json = new Gson().toJson(game.game();
            preparedStatement.setString(4,json);
            preparedStatement.setInt(5,id);
            preparedStatement.executeUpdate();

        } catch (SQLException x) {
            throw new DataAccessException("Error: " + x.getMessage());
        }
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        ArrayList<GameData> games = new ArrayList<>();
        try(Connection connection = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName FROM game");
            try(ResultSet allGames = preparedStatement.executeQuery()){
                while(allGames.next()){
                    int gameID = allGames.getInt("gameID");
                    String whiteUsername = allGames.getString("whiteUsername");
                    String blackUsername = allGames.getString("blackUsername");
                    String gameName = allGames.getString("gameName");
                    games.add(new GameData(gameID,whiteUsername,blackUsername,gameName,null));

                }
                return games;
            }
        } catch (SQLException x) {
            throw new DataAccessException("Error: " + x.getMessage());
        }
    }

    @Override
    public int size() {
        try(Connection connection = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM game");
            ResultSet queryResult = preparedStatement.executeQuery();
            if(queryResult.next()) {
                return queryResult.getInt(1);
            }
        } catch(SQLException | DataAccessException ex) {
            return -1;
        }
        return -1;
    }
}
