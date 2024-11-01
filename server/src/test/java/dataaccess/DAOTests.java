package dataaccess;
import models.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearGameService;

import java.util.ArrayList;

public class DAOTests {

    private Database db;

    @BeforeEach
    public void setUp() {
        db = new Database();
        ClearGameService clearGameService = new ClearGameService(db);
        clearGameService.deleteAll();
    }

    private void createUserHelper() throws Exception {
        db.createUser("name", "password", "email");
    }

    private void createAuthHelper() throws Exception {
        db.createAuth("1234", "name");
    }

    private void createGameHelper() throws Exception {
        db.createGame("testGame");
    }

    // Positive Tests

    @Test
    public void testCreateUser() {
        try {
            createUserHelper();
            Assertions.assertNotNull(db.getUser("name"));
            Assertions.assertEquals(1, db.userDataBase.size());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testCreateAuth() {
        try {
            createAuthHelper();
            Assertions.assertNotNull(db.getAuth("1234"));
            Assertions.assertEquals(1, db.authDataBase.size());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testCreateGame() {
        try {
            createGameHelper();
            Assertions.assertNotNull(db.getGame(1));
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

   
}