package service;
import dataaccess.*;
import result.*;

public class ClearGameService {

    Database database;

    public ClearGameService(Database database) {
        this.database = database;
    }

    public ClearResult deleteAll()  {
        ClearResult result;
        try {
            database.clearAll();
            if(database.isAllEmpty())
                result = new ClearResult(true, null);
            else
                throw new DataAccessException("Didn't clear the database");
        } catch(DataAccessException ex) {
            result = new ClearResult(false, "Error: " + ex.getMessage());
        }
        return result;
    }
}