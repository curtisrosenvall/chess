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
        String sql = "TRUNCATE user";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.executeUpdate();

        } catch (SQLException exception) {

            throw new DataAccessException(exception.getMessage());
        }
    }

    @Override
    public void createUser(String name, UserData authData) throws DataAccessException {
        String sql = "INSERT INTO user (username, password, email, json) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, authData.password());
            statement.setString(3, authData.email());

            String json = new Gson().toJson(authData);
            statement.setString(4, json);

            statement.executeUpdate();

        } catch (SQLException exception) {
            throw new DataAccessException( exception.getMessage());
        }
    }

    @Override
    public UserData getUser(String name) throws DataAccessException {
        String sql = "SELECT json FROM user WHERE username = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, name);

            try (ResultSet queryResult = statement.executeQuery()) {

                if (queryResult.next()) {
                    String json = queryResult.getString("json");
                    return new Gson().fromJson(json, UserData.class);
                }
            }

        } catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
        return null;
    }

    @Override
    public int size() {
        String sql = "SELECT COUNT(*) FROM user";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet queryResult = statement.executeQuery()) {

            if (queryResult.next()) {
                return queryResult.getInt(1);
            }

        } catch (SQLException | DataAccessException ex) {
            return -1;
        }

        return -1;
    }
}