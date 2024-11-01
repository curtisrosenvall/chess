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

    @Test
    public void testJoinGameWhite() {
        try {
            createGameHelper();
            GameData game = db.getGame(1);
            GameData updatedGame = new GameData(
                    1,
                    "White Player",
                    game.blackUsername(),
                    game.gameName(),
                    game.game()
            );
            db.updateGame(updatedGame);
            GameData newGame = db.getGame(1);
            Assertions.assertNotEquals(newGame.whiteUsername(), game.whiteUsername());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testJoinGameBlack() {
        try {
            createGameHelper();
            GameData game = db.getGame(1);
            GameData updatedGame = new GameData(
                    1,
                    game.whiteUsername(),
                    "Black Player",
                    game.gameName(),
                    game.game()
            );
            db.updateGame(updatedGame);
            GameData newGame = db.getGame(1);
            Assertions.assertNotEquals(newGame.blackUsername(), game.blackUsername());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testDeleteAuth() {
        try {
            createAuthHelper();
            db.deleteAuth("1234");
            Assertions.assertEquals(0, db.authDataBase.size());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testListGames() {
        try {
            db.createGame("firstGame");
            db.createGame("secondGame");
            Assertions.assertEquals(2, db.gameDataBase.size());
            ArrayList<GameData> games = db.getGameList();
            Assertions.assertNotNull(games);
            Assertions.assertEquals(2, games.size());
        } catch (Exception ex) {
            Assertions.fail(ex.getMessage());
        }
    }

    @Test
    public void testListOneGameSuccess() {
        try {
            createGameHelper();
            ArrayList<GameData> games = db.getGameList();
            Assertions.assertNotNull(games);
            Assertions.assertEquals(1, games.size());
        } catch (Exception ex) {
            Assertions.fail(ex.getMessage());
        }
    }

    @Test
    public void testClearAll() {
        try {
            createAuthHelper();
            createGameHelper();
            createUserHelper();
            db.clearAll();
            Assertions.assertTrue(db.isAllEmpty());
        } catch (Exception ex) {
            Assertions.fail(ex.getMessage());
        }
    }

    // Negative Tests

    @Test
    public void testCreateExistingUser() {
        try {
            createUserHelper();
            db.createUser("name", "1234", "email");
            Assertions.fail("Expected exception for creating an existing user");
        } catch (Exception ex) {
            Assertions.assertTrue(ex.getMessage().contains("Username already taken"));
        }
    }

    @Test
    public void testGetNonExistingUser() {
        try {
            db.getUser("nonexistent");
            Assertions.fail("Expected exception for retrieving a non-existing user");
        } catch (Exception ex) {
            Assertions.assertTrue(ex.getMessage().contains("Invalid username"));
        }
    }

    @Test
    public void testCreateExistingAuth() {
        try {
            createAuthHelper();
            db.createAuth("1234", "test");
            Assertions.fail("Expected exception for creating an existing auth token");
        } catch (Exception ex) {
            Assertions.assertTrue(ex.getMessage().contains("Auth token already taken"));
        }
    }

    @Test
    public void testListNoGames() {
        try {
            ArrayList<GameData> games = db.getGameList();
            Assertions.assertNotNull(games);
            Assertions.assertEquals(0, games.size());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testCreateExistingGame() {
        try {
            createGameHelper();
            db.createGame("testGame");
            Assertions.fail("Expected exception for creating an existing game");
        } catch (Exception ex) {
            Assertions.assertTrue(ex.getMessage().contains("Game name already taken"));
        }
    }

    @Test
    public void testDeleteNonExistingAuth() {
        try {
            db.deleteAuth("1234");
            Assertions.fail("Expected exception for deleting a non-existing auth token");
        } catch (Exception ex) {
            Assertions.assertTrue(ex.getMessage().contains("Invalid token"));
        }
    }

    @Test
    public void testGetNonExistingAuth() {
        try {
            db.getAuth("1234");
            Assertions.fail("Expected exception for retrieving a non-existing auth token");
        } catch (Exception ex) {
            Assertions.assertTrue(ex.getMessage().contains("Invalid token"));
        }
    }

    @Test
    public void testGetNonExistingGame() {
        try {
            db.getGameName("nonexistentGame");
            Assertions.fail("Expected exception for retrieving a non-existing game");
        } catch (Exception ex) {
            Assertions.assertTrue(ex.getMessage().contains("Invalid game name"));
        }
    }

    @Test
    public void testUpdateNonExistingGame() {
        try {

            GameData game = db.getGame(999);

            GameData nonExistingGame = new GameData(
                    999,
                    "Player1",
                    "Player2",
                    "NonExistingGame",
                    game.game()
            );
            db.updateGame(nonExistingGame);
            Assertions.fail("Expected exception for updating a non-existing game");
        } catch (Exception ex) {
            Assertions.assertTrue(ex.getMessage().contains("Invalid game ID"));
        }
    }

    @Test
    public void testUpdateGameSuccess() {
        try {
            createGameHelper();
            GameData game = db.getGame(1);

            GameData updatedGame = new GameData(
                    game.gameID(),
                    "NewWhitePlayer",
                    "NewBlackPlayer",
                    "UpdatedGameName",
                    game.game()
            );
            db.updateGame(updatedGame);

            GameData newGame = db.getGame(1);
            Assertions.assertEquals("NewWhitePlayer", newGame.whiteUsername());
            Assertions.assertEquals("NewBlackPlayer", newGame.blackUsername());
            Assertions.assertEquals("UpdatedGameName", newGame.gameName());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }
}