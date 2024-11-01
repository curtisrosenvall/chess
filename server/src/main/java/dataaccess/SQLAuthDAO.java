package dataaccess;

import com.google.gson.Gson;
import models.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO {

    @Override
    public void clear() throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("TRUNCATE auth");
            statement.executeUpdate();
        } catch(SQLException exception) {
            throw new DataAccessException("Error: " + exception.getMessage());
        }
    }

    @Override
    public void createAuth(String token, AuthData data) throws DataAccessException{
        try(Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO auth (authToken, username, json) VALUES (?,?,?)");
            statement.setString(1,token);
            statement.setString(2, data.username());
            String json = new Gson().toJson(data);
            statement.setObject(3, json);
            statement.executeUpdate();
        } catch(SQLException ex) {
            throw new DataAccessException("Error " + ex.getMessage());
        }
    }


    @Override
    public void deleteAuth(String token){

    }

    @Override
    public AuthData getAuth(String token) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()){
            PreparedStatement statement = conn.prepareStatement("SELECT json FROM auth WHERE authToken=?");
            statement.setString(1,token);
            ResultSet queryResult = statement.executeQuery();
            if(queryResult.next()) {
                String json = queryResult.getString("json");
                return new Gson().fromJson(json,AuthData.class);
            }
        } catch (SQLException ex){
            throw new DataAccessException("Error " + ex.getMessage());
        }
        return null;
    }

    @Override
    public int size(){
        return 1;
    }
}
