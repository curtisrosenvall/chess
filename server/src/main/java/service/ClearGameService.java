package service;

import dataaccess.*;
import result.*;

public class ClearGameService {

    Database database;

    public ClearGameService(Database database) {
        this.database = database;
    }

    public ClearRes deleteAll()  {
        ClearRes result;
        try {
            database.clearAll();
            if(database.isAllEmpty()) {
                result = new ClearRes(true, null);}
            else {
                throw new DataAccessException("Didn't clear the database");
            }
        } catch(DataAccessException ex) {
            result = new ClearRes(false, "Error: " + ex.getMessage());
        }
        return result;
    }
}