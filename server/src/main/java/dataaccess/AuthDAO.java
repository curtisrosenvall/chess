package dataaccess;

import model.AuthData;

import java.util.ArrayList;

/**
 * The AuthDAO interface provides methods for interacting with
 * authentication data in the database. Implementations of this
 * interface should handle the necessary database operations for
 * creating, reading, updating, and deleting authentication data.
 */
public interface AuthDAO {

    /**
     * Clears all authentication data from the database.
     *
     * @throws DataAccessException if there is an error during the database operation
     */
    void clear() throws DataAccessException;

    /**
     * Creates a new authentication entry in the database.
     *
     * @param token the authentication token
     * @param data the authentication data associated with the token
     * @throws DataAccessException if there is an error during the database operation
     */
    void createAuth(String token, AuthData data) throws DataAccessException;

    /**
     * Deletes an authentication entry from the database.
     *
     * @param token the authentication token
     * @throws DataAccessException if there is an error during the database operation
     */
    void deleteAuth(String token) throws DataAccessException;

    /**
     * Retrieves authentication data from the database based on the provided token.
     *
     * @param token the authentication token
     * @return the authentication data associated with the token, or null if no data is found
     * @throws DataAccessException if there is an error during the database operation
     */
    AuthData getAuth(String token) throws DataAccessException;

    /**
     * Returns the number of authentication entries in the database.
     *
     * @return the number of authentication entries
     */
    int size();

    /**
     * Retrieves all authentication entries from the database.
     *
     * @return an ArrayList containing all authentication data entries
     */
    ArrayList<AuthData> getAllAuths();
}