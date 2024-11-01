package dataaccess;

import java.sql.*;
import models.*;
import com.google.gson.Gson;


public class SQLUserDAO implements UserDAO {


    @Override
    public void clear() throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("TRUNCATE user");
            statement.executeUpdate();
        } catch(SQLException exception) {
            throw new DataAccessException("Error: " + exception.getMessage());
        }
    }

    @Override
    public void createUser(String name, UserData authData) throws DataAccessException{
        try(Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO user (username, password, email, json) VALUES (?,?,?,?)");
            statement.setString(1,name);
            statement.setString(2,authData.password());
            statement.setString(3,authData.email());
            String json = new Gson().toJson(authData);
            statement.setString(4, json);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("error" + ex.getMessage());
        }
    }

    @Override
    public UserData getUser(String name){
        return null;
    }


    @Override
    public int size(){
        return 1;
    }




}
