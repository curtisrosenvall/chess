package dataaccess;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearGameService;

public class DatabaseDAOTest {

    Database database;

    @BeforeEach
    public void createDatabase() {
        database = new Database();
        ClearGameService clearGameService = new ClearGameService(database);
        clearGameService.deleteAll();
    }

//    Positive tests
    @Test
    public void  createUser() {
        try{
            database.createUser("name","password", "email");
            Assertions.assertNotNull(database.getUser("name"));
            Assertions.assertEquals(1,database.userDataBase.size());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void createAuth() {
        try{
            database.createAuth("1234", "name");
            Assertions.assertNotNull(database.getAuth("1234"));
            Assertions.assertEquals(1,database.authDataBase.size());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test void createGame() {
        try {
            database.createGame("testGame");
            Assertions.assertNotNull(database.getGame(1));
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void joinGameWhite() {
        try{
            createGame();
            GameData game = database.getGame(1);
            database.updateGame(new GameData(1, "White Player", game.blackUsername(), game.gameName(), game.game()));
            GameData newGame = database.getGame(1);
            Assertions.assertNotEquals(newGame.whiteUsername(), game.whiteUsername());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    public void joinGameBlack() {
        try{
            createGame();
            GameData game = database.getGame(1);
            database.updateGame(new GameData(1,  game.whiteUsername(),"Black Player", game.gameName(), game.game()));
            GameData newGame = database.getGame(1);
            Assertions.assertNotEquals(newGame.blackUsername(), game.blackUsername());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void deleteAuth(){
        try {
            createAuth();
            database.deleteAuth("1234");
            Assertions.assertEquals(0,database.authDataBase.size());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void listGames() {
        try{
            database.createGame("firstGame");
            database.createGame("secondGame");
            Assertions.assertEquals(2, database.gameDataBase.size());
            database.getGameList();
        } catch (Exception ex) {
            Assertions.fail(ex.getMessage());
        }


    }

    @Test
    public void clearUsedDAOs() {
        try {
            database.createAuth("1234","test");
            createGame();
            createUser();
            database.clearAll();
        } catch(Exception ex) {
            Assertions.fail();
        }
        Assertions.assertTrue(database.isAllEmpty());
    }

//    Negative

    @Test
    public void createExistingUser() {
        try {
            createUser();
            database.createUser("name", "1234", "email");
        } catch(Exception ex) {
            Assertions.assertTrue(ex.getMessage().contains("already taken"));
        }
    }

    @Test
    public void createExistingAuth() {
        try{
            createAuth();
            database.createAuth("1234", "test");
            Assertions.fail();
        } catch(Exception ex) {
            Assertions.assertTrue(ex.getMessage().contains("Auth taken"));
        }
    }

    @Test
    public void listNoGames() {
        try{
            database.getGameList();
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void createExistingGame() {
        try{
            createGame();
            database.createGame("testGame");
            Assertions.fail();
        } catch(Exception ex) {
            Assertions.assertTrue(ex.getMessage().contains("Game taken"));
        }
    }

    @Test
    public void deleteNonExistingAuth() {
        try {
            database.deleteAuth("1234");
        } catch(Exception ex) {
            Assertions.assertTrue(ex.getMessage().contains("Not valid token"));
        }
    }

    @Test
    public void getNonExistingAuth() {
        try {
            database.getAuth("1234");
        } catch(Exception ex) {
            Assertions.assertTrue(ex.getMessage().contains("Not valid"));
        }
    }

    @Test
    public void getNonExistingGame() {
        try {
            database.getGameName("name");
        } catch(Exception ex) {
            Assertions.assertTrue(ex.getMessage().contains("Not valid Game"));
        }
    }
}
