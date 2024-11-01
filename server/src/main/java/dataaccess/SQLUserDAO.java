package dataaccess;

import com.google.gson.Gson;
import models.UserData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {

    @Override
    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("TRUNCATE user");
            statement.executeUpdate();
        } catch(SQLException ex) {
            throw new DataAccessException("Error: " + ex.getMessage());
        }
    }

    @Override
    public void createUser(String name, UserData authData) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO user (username, password, email, json) VALUES (?,?,?,?)");
            statement.setString(1,name);
            statement.setString(2, authData.password());
            statement.setString(3, authData.email());
            String json = new Gson().toJson(authData);
            statement.setString(4, json);
            statement.executeUpdate();
        } catch(SQLException ex) {
            throw new DataAccessException("Error: " + ex.getMessage());
        }
    }

    @Override
    public UserData getUser(String name) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("SELECT json FROM user WHERE username=?");
            statement.setString(1,name);
            ResultSet queryResult = statement.executeQuery();
            if(queryResult.next()) {
                String json = queryResult.getString("json");
                return new Gson().fromJson(json, UserData.class);
            }
        } catch(SQLException ex) {
            throw new DataAccessException("Error: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public int size() {
        try(Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("SELECT COUNT(*) FROM user");
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