package service;

import dataaccess.*;
import org.junit.jupiter.api.*;
import result.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ClearGameService.
 */
class ClearGameServiceTest extends ParentTest {

    private Database db;
    private ClearGameService clear;
    private ClearRes result;

    @BeforeEach
    public void setUp() {
        db = new Database();
        clear = new ClearGameService(db);
    }

    @Test
    public void clearAllWithUser() {
        try {
            db.createUser("Test", "1234", "test@curtis.com");
            result = clear.deleteAll();
            assertTrue(result.isSuccess(), result.getMessage());
        } catch (DataAccessException ex) {
            fail("Unexpected DataAccessException: " + ex.getMessage());
        }
    }

    @Test
    public void clearAllWithGame() {
        try {
            db.createGame("Test");
            result = clear.deleteAll();
            assertTrue(result.isSuccess(), result.getMessage());
        } catch (DataAccessException ex) {
            fail("Unexpected DataAccessException: " + ex.getMessage());
        }
    }

    @Test
    public void clearAllWithAuth() {
        try {
            db.createAuth("TOKEN", "Test");
            result = clear.deleteAll();
            assertTrue(result.isSuccess(), result.getMessage());
        } catch (DataAccessException ex) {
            fail("Unexpected DataAccessException: " + ex.getMessage());
        }
    }

    @Test
    public void clearAllWhenEmpty() {
        result = clear.deleteAll();
        assertTrue(result.isSuccess(), result.getMessage());
    }
}