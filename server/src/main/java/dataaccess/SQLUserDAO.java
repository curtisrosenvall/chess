package dataaccess;

import java.sql.*;
import models.*;

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
    public void createUser(String name, UserData userData){

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
