package dataaccess;

import model.GameData;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * GameDAO is an interface that defines the contract for Data Access Object (DAO)
 * to perform CRUD (Create, Read, Update, Delete) operations on GameData objects.
 */
public interface GameDAO {

    /**
     * Clears all game data from the data store.
     *
     * @throws DataAccessException if an error occurs during the data access operation.
     */
    void clear() throws DataAccessException;

    /**
     * Creates a new game with the specified name in the data store.
     *
     * @param name the name of the new game to be created.
     * @throws DataAccessException if an error occurs during the data access operation.
     */
    void createGame(String name) throws DataAccessException;

    /**
     * Retrieves the game data for a specific game identified by its unique ID.
     *
     * @param id the unique identifier of the game to retrieve.
     * @return the GameData object for the specified game.
     * @throws DataAccessException if an error occurs during the data access operation.
     */
    GameData getGame(int id) throws DataAccessException;

    /**
     * Retrieves all game data from the data store.
     *
     * @return an ArrayList of GameData objects representing all games.
     * @throws DataAccessException if an error occurs during the data access operation.
     */
    ArrayList<GameData> getAllGames() throws DataAccessException;

    /**
     * Updates the game data for a specific game identified by its unique ID with the provided game data.
     *
     * @param id the unique identifier of the game to update.
     * @param game the GameData object containing the updated data for the game.
     * @throws DataAccessException if an error occurs during the data access operation.
     */
    void updateGame(int id, GameData game) throws DataAccessException;


    /**
     * Returns the number of games currently stored in the data store.
     *
     * @return the number of games.
     */
    int size();
}