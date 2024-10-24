package dataaccess;

import models.UserData;

public interface UserDAO {

    void clear() throws DataAccessException;
    void createUser(String name, UserData authData) throws DataAccessException;
    UserData getUser(String name) throws DataAccessException;
    int size();

}