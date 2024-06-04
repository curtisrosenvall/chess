package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
