package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.*;
import result.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for GameService.
 */
public class GameServiceTest extends ParentTest {

    private CreateGameRes createResult;
    private JoinGameRes joinResult;
    private RegisterRes registerResult;

    @BeforeEach
    public void setUp() {
        createDataBase();
    }

    @Test
    public void createNewGame() {
        CreateGameRes gameResult = gameService.createGame(new CreateGameReq(authToken, "Test"));
        assertTrue(gameResult.isSuccess(), gameResult.getMessage());
    }

    @Test
    public void createExistingGame() {
        createNewGame();
        CreateGameRes gameResult = gameService.createGame(new CreateGameReq(authToken, "Test"));
        assertFalse(gameResult.isSuccess(), gameResult.getMessage());
    }

    @Test
    public void createGameInvalidAuth() {
        CreateGameRes gameResult = gameService.createGame(new CreateGameReq("1234", "Test"));
        assertFalse(gameResult.isSuccess(), gameResult.getMessage());
    }

    @Test
    public void createGameAndJoinWhite() {
        createResult = gameService.createGame(new CreateGameReq(authToken, "NEWGAME!"));
        assertTrue(createResult.isSuccess(), createResult.getMessage());

        joinResult = gameService.joinGame(
                new JoinGameReq(authToken, "WHITE", createResult.getGameId())
        );
        assertTrue(joinResult.isSuccess(), joinResult.getMessage());
    }

    @Test
    public void createGameAndJoinBlack() {
        createResult = gameService.createGame(new CreateGameReq(authToken, "another game!"));
        assertTrue(createResult.isSuccess(), createResult.getMessage());

        joinResult = gameService.joinGame(
                new JoinGameReq(authToken, "BLACK", createResult.getGameId())
        );
        assertTrue(joinResult.isSuccess(), joinResult.getMessage());
    }

    @Test
    public void noCreateTryJoin() {
        joinResult = gameService.joinGame(new JoinGameReq(authToken, "WHITE", 10));
        assertFalse(joinResult.isSuccess(), joinResult.getMessage());
    }

    @Test
    public void invalidAuth() {
        createResult = gameService.createGame(new CreateGameReq(authToken, "Test"));
        assertTrue(createResult.isSuccess(), createResult.getMessage());

        joinResult = gameService.joinGame(
                new JoinGameReq("1234", "WHITE", createResult.getGameId())
        );
        assertFalse(joinResult.isSuccess(), joinResult.getMessage());
    }

    @Test
    public void colorAlreadyTaken() {
        createResult = gameService.createGame(new CreateGameReq(authToken, "Fun Game"));
        assertTrue(createResult.isSuccess(), createResult.getMessage());

        joinResult = gameService.joinGame(
                new JoinGameReq(authToken, "BLACK", createResult.getGameId())
        );
        assertTrue(joinResult.isSuccess(), joinResult.getMessage());

        registerResult = userService.createUser(
                new RegisterReq("NewGuy", "9081726354", "lol@gmail.com")
        );
        assertTrue(registerResult.isSuccess(), registerResult.getMessage());

        String newToken = registerResult.getAuthToken();
        joinResult = gameService.joinGame(
                new JoinGameReq(newToken, "BLACK", createResult.getGameId())
        );
        assertFalse(joinResult.isSuccess(), joinResult.getMessage());
    }

    @Test
    public void createGameAddBothPlayers() {
        createResult = gameService.createGame(
                new CreateGameReq(authToken, "Full Game, Let's PLAY")
        );
        assertTrue(createResult.isSuccess(), createResult.getMessage());

        joinResult = gameService.joinGame(
                new JoinGameReq(authToken, "BLACK", createResult.getGameId())
        );
        assertTrue(joinResult.isSuccess(), joinResult.getMessage());

        registerResult = userService.createUser(
                new RegisterReq("NewGuy", "9081726354", "lol@gmail.com")
        );
        assertTrue(registerResult.isSuccess(), registerResult.getMessage());

        String newToken = registerResult.getAuthToken();
        joinResult = gameService.joinGame(
                new JoinGameReq(newToken, "WHITE", createResult.getGameId())
        );
        assertTrue(joinResult.isSuccess(), joinResult.getMessage());
    }
}