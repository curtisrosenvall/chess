package dataaccess;
import com.google.gson.Gson;
import model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLAuthDAO  implements AuthDAO {

    @Override
    public void clear() throws DataAccessException {
        try(Connection connection = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("TRUNCATE auth");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }

    @Override
    public void createAuth(String token, AuthData data) throws DataAccessException {
        try(Connection connection = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO auth (authToken, username, json) VALUES (?,?,?)");
            preparedStatement.setString(1, token);
            preparedStatement.setString(2, data.username());
            String json = new Gson().toJson(data);
            preparedStatement.setObject(3, json);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAuth(String token) throws DataAccessException {
        try(Connection connection = DatabaseManager.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM auth WHERE authToken=?");
            preparedStatement.setString(1,token);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }

    

    @Override
    public int size() {
        try(Connection connection = DatabaseManager.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT  COUNT(*) FROM auth");
            ResultSet queryResult = preparedStatement.executeQuery();
            if(queryResult.next()) {
                return queryResult.getInt(1);
            }
        } catch (SQLException | DataAccessException ex) {
            return -1;
        }
        return -1;
    }


}
