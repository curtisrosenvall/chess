package service;

import dataaccess.*;
import org.junit.jupiter.api.*;
import result.ClearResult;


import static org.junit.jupiter.api.Assertions.*;

class ClearGameServiceTest extends ParentTest {

    Database db;
    ClearGameService clear;
    ClearResult result;

    @BeforeEach
    public void createDataBase() {
        db = new Database();
        clear = new ClearGameService(db);
        ClearResult result;
    }

    @Test
    public void clearAll1User() {
        try {
            db.createUser("Test", "1234", "test@gmail.com");
            result = clear.deleteAll();
            Assertions.assertTrue(result.isSuccess(), result.getMessage());
        } catch(DataAccessException ex) {
            fail("Should not have caught a DataAccessException: " + ex.getMessage());
        }
    }

    @Test
    public void clearAll1Game() {
        try {
            db.createGame("Test");
            result = clear.deleteAll();
            Assertions.assertTrue(result.isSuccess(), result.getMessage());
        } catch(DataAccessException ex) {
            fail("Should not have caught a DataAccessException: " + ex.getMessage());
        }
    }

    @Test
    public void clearAll1Auth() {
        try {
            db.createAuth("TOKEN", "Test");
            result = clear.deleteAll();
            Assertions.assertTrue(result.isSuccess(), result.getMessage());
        } catch(DataAccessException ex) {
            fail("Should not have caught a DataAccessException: " + ex.getMessage());
        }
    }

    @Test
    public void clearAllEmpty() {
        ClearGameService clear = new ClearGameService(db);
        result = clear.deleteAll();
        Assertions.assertTrue(result.isSuccess(), result.getMessage());
    }

}