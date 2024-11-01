package dataaccess;

import models.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
    public void createAuth(String name, AuthData data){
        return;
    }


    @Override
    public void deleteAuth(String token){

    }

    @Override
    public AuthData getAuth(String token) {
        return null;
    }

    @Override
    public int size(){
        return 1;
    }
}
