package dataaccess;

import model.AuthData;

import java.util.ArrayList;

public interface AuthDAO {

    void clear() throws DataAccessException;
    void createAuth(String token, AuthData data) throws DataAccessException;
    void deleteAuth(String token) throws DataAccessException;
    AuthData getAuth(String token) throws DataAccessException;
    int size();
    ArrayList<AuthData> getAllAuths();

}
