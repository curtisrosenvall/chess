package dataaccess;

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
        throw new DataAccessException("Error: ");
    }

    @Override
    public GameData getGame(int id) {
        return null;
    }

    @Override
    public ArrayList<GameData> listGames(){
        return null;
    }

    @Override
    public void updateGame(int id, GameData game){

    }

    @Override
    public int size(){
        return 1;
    }
}
