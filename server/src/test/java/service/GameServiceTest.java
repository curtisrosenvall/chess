package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import result.CreateGameResult;

import request.*;
import result.*;


public class GameServiceTest extends ParentTest{

    CreateGameResult gameResult;
    private CreateGameResult createResult;
    private JoinGameResult joinResult;
    private RegisterResult registerResult;

    @BeforeEach
    public void setUp() {
        createDataBase();
    }

    @Test
    public void createNewGame() {
        gameResult = gameService.createGame(new CreateGameRequest(authToken, "Test"));
        Assertions.assertTrue(gameResult.isSuccess(), gameResult.getMessage());
    }

    @Test
    public void createExistingGame() {
        createNewGame();
        gameResult = gameService.createGame(new CreateGameRequest(authToken, "Test"));
        Assertions.assertFalse(gameResult.isSuccess(), gameResult.getMessage());
    }

    @Test
    public void createGameInvalidAuth() {
        gameResult = gameService.createGame(new CreateGameRequest("1234", "Test"));
        Assertions.assertFalse(gameResult.isSuccess(), gameResult.getMessage());
    }

    @Test
    public void createGameAndJoinWHITE() {
        createResult = gameService.createGame(new CreateGameRequest(authToken, "NEWGAME!"));
        Assertions.assertTrue(createResult.isSuccess(), createResult.getMessage());
        joinResult = gameService.joinGame(new JoinGameRequest(authToken, "WHITE", createResult.getGameId()));
        Assertions.assertTrue(joinResult.isSuccess(), joinResult.getMessage());
    }

    @Test
    public void createGameAndJoinBLACK() {
        createResult = gameService.createGame(new CreateGameRequest(authToken, "another game!"));
        Assertions.assertTrue(createResult.isSuccess(), createResult.getMessage());
        joinResult = gameService.joinGame(new JoinGameRequest(authToken, "BLACK", createResult.getGameId()));
        Assertions.assertTrue(joinResult.isSuccess(), joinResult.getMessage());
    }

    @Test
    public void noCreateTryJoin() {
        joinResult = gameService.joinGame(new JoinGameRequest(authToken, "WHITE", 10));
        Assertions.assertFalse(joinResult.isSuccess(), joinResult.getMessage());
    }

    @Test
    public void invalidAuth() {
        createResult = gameService.createGame(new CreateGameRequest(authToken, "Test"));
        Assertions.assertTrue(createResult.isSuccess(), createResult.getMessage());
        joinResult = gameService.joinGame(new JoinGameRequest("1234", "WHITE", createResult.getGameId()));
        Assertions.assertFalse(joinResult.isSuccess(), joinResult.getMessage());
    }

    //TeamAlreadyTaken
    @Test
    public void colorAlreadyTaken() {
        createResult = gameService.createGame(new CreateGameRequest(authToken, "Fun Game"));
        Assertions.assertTrue(createResult.isSuccess(), createResult.getMessage());
        joinResult = gameService.joinGame(new JoinGameRequest(authToken, "BLACK", createResult.getGameId()));
        Assertions.assertTrue(joinResult.isSuccess(), joinResult.getMessage());
        registerResult = userService.createUser(new RegisterRequest("NewGuy", "9081726354", "lol@gmail.com"));
        Assertions.assertTrue(registerResult.isSuccess(), registerResult.getMessage());
        String newToken = registerResult.getAuthToken();
        joinResult = gameService.joinGame(new JoinGameRequest(newToken, "BLACK", createResult.getGameId()));
        Assertions.assertFalse(joinResult.isSuccess(), joinResult.getMessage());
    }

    @Test
    public void createGameAddBoth() {
        createResult = gameService.createGame(new CreateGameRequest(authToken, "Full Game, Let's PLAY"));
        Assertions.assertTrue(createResult.isSuccess(), createResult.getMessage());
        joinResult = gameService.joinGame(new JoinGameRequest(authToken, "BLACK", createResult.getGameId()));
        Assertions.assertTrue(joinResult.isSuccess(), joinResult.getMessage());
        registerResult = userService.createUser(new RegisterRequest("NewGuy", "9081726354", "lol@gmail.com"));
        Assertions.assertTrue(registerResult.isSuccess(), registerResult.getMessage());
        String newToken = registerResult.getAuthToken();
        joinResult = gameService.joinGame(new JoinGameRequest(newToken, "WHITE", createResult.getGameId()));
        Assertions.assertTrue(joinResult.isSuccess(), joinResult.getMessage());
    }
}