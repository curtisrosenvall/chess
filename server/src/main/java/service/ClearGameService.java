package service;
import dataaccess.*;
import response.ClearResponse;




public class ClearGameService {

    Database database;

    public ClearGameService(Database database) {
        this.database = database;
    }

    public ClearResponse deleteAll()  {
        ClearResponse result;
        try {
            database.clearAll();
            if(database.isAllEmpty())
                result = new ClearResponse(true, null);
            else
                throw new DataAccessException("Didn't clear the database");
        } catch(DataAccessException ex) {
            result = new ClearResponse(false, "Error: " + ex.getMessage());
        }
        return result;
    }
}