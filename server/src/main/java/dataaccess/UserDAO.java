package dataaccess;

import model.UserData;

/**
 * UserDAO is an interface that defines the methods for interacting with user data in the database.
 * It provides methods for creating, retrieving, deleting, and clearing user data, as well as
 * checking the size of the user database.
 */
public interface UserDAO {

    /**
     * Clears all user data from the database.
     * @throws DataAccessException if there is an error while clearing the data.
     */
    void clear() throws DataAccessException;

    /**
     * Creates a new user in the database with the specified name and authentication data.
     * @param name the name of the user to be created.
     * @param authData the authentication data for the user.
     * @throws DataAccessException if there is an error while creating the user.
     */
    void createUser(String name, UserData authData) throws DataAccessException;

    /**
     * Deletes the user with the specified name from the database.
     * @param name the name of the user to be deleted.
     * @throws DataAccessException if there is an error while deleting the user.
     */
    void deleteUser(String name) throws DataAccessException;

    /**
     * Retrieves the user data for the user with the specified name.
     * @param name the name of the user whose data is to be retrieved.
     * @return the UserData object containing the user's data.
     * @throws DataAccessException if there is an error while retrieving the user data.
     */
    UserData getUser(String name) throws DataAccessException;

    /**
     * Returns the number of users in the database.
     * @return the number of users.
     */
    int size();
}