package client;

import org.junit.jupiter.api.*;
import result.*;
import result.RegisterRes;
import server.Server;
import facade.*;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        int port = server.run(0);
        System.out.println("Started test HTTP server on port " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    public void setUp() {
        facade.clear();
    }

    @AfterAll
    public static void stopServer() {
        server.stop();
    }

    // Helper methods
    private String registerUser(String username, String password, String email) {
        RegisterRes result = facade.registerUser(username, password, email);
        assertNull(result.getMessage(), "Registration failed: " + result.getMessage());
        return result.getAuthToken();
    }

    private String loginUser(String username, String password) {
        LoginRes result = facade.loginUser(username, password);
        assertNull(result.getMessage(), "Login failed: " + result.getMessage());
        return result.getAuthToken();
    }

    private void logoutUser(String authToken) {
        LogoutRes result = facade.logoutUser(authToken);
        assertNull(result.getMessage(), "Logout failed: " + result.getMessage());
    }

    private int createGame(String gameName, String authToken) {
        CreateGameRes result = facade.createGame(gameName, authToken);
        assertNull(result.getMessage(), "Create game failed: " + result.getMessage());
        assertNotNull(result.getGameId(), "Game ID should not be null");
        return result.getGameId();
    }

    // Tests

    @Test
    public void testRegisterUser() {
        String authToken = registerUser("user1", "pass1", "email1@example.com");
        assertNotNull(authToken, "Auth token should not be null after registration");
    }

    @Test
    public void testLoginUser() {
        registerUser("user2", "pass2", "email2@example.com");
        String authToken = loginUser("user2", "pass2");
        assertNotNull(authToken, "Auth token should not be null after login");
    }

    @Test
    public void testLogoutUser() {
        String authToken = registerUser("user3", "pass3", "email3@example.com");
        logoutUser(authToken);
    }

    @Test
    public void testRegisterMultipleUsers() {
        String authToken1 = registerUser("user4", "pass4", "email4@example.com");
        String authToken2 = registerUser("user5", "pass5", "email5@example.com");
        assertNotEquals(authToken1, authToken2, "Auth tokens for different users should not be equal");
    }

    @Test
    public void testCreateGame() {
        String authToken = registerUser("user6", "pass6", "email6@example.com");
        int gameId = createGame("Game1", authToken);
        assertTrue(gameId > 0, "Game ID should be greater than zero");
    }

    @Test
    public void testListOneGame() {
        String authToken = registerUser("user7", "pass7", "email7@example.com");
        createGame("Game2", authToken);
        ListGamesRes result = facade.listGames(authToken);
        assertNull(result.getMessage(), "List games failed: " + result.getMessage());
        assertEquals(1, result.getGames().size(), "Should have one game in the list");
    }

    @Test
    public void testListMultipleGames() {
        String authToken = registerUser("user8", "pass8", "email8@example.com");
        createGame("Game3", authToken);
        createGame("Game4", authToken);
        createGame("Game5", authToken);
        ListGamesRes result = facade.listGames(authToken);
        assertNull(result.getMessage(), "List games failed: " + result.getMessage());
        assertEquals(3, result.getGames().size(), "Should have three games in the list");
    }

    @Test
    public void testJoinEmptyWhite() {
        String authToken = registerUser("user9", "pass9", "email9@example.com");
        int gameId = createGame("Game6", authToken);
        JoinGameRes result = facade.joinGame(gameId, "WHITE", authToken);
        assertNull(result.getMessage(), "Join game failed: " + result.getMessage());
    }

    @Test
    public void testJoinEmptyBlack() {
        String authToken = registerUser("user10", "pass10", "email10@example.com");
        int gameId = createGame("Game7", authToken);
        JoinGameRes result = facade.joinGame(gameId, "BLACK", authToken);
        assertNull(result.getMessage(), "Join game failed: " + result.getMessage());
    }

    @Test
    public void testRegisterExistingUser() {
        registerUser("user11", "pass11", "email11@example.com");
        RegisterRes result = facade.registerUser("user11", "pass11", "email11@example.com");
        assertNotNull(result.getMessage(), "Should not allow registering an existing user");
    }

    @Test
    public void testRegisterIncomplete() {
        RegisterRes result = facade.registerUser(null, "pass12", "email12@example.com");
        assertNotNull(result.getMessage(), "Should not allow registration with null username");
    }

    @Test
    public void testLoginNonExistingUser() {
        LoginRes result = facade.loginUser("nonexistentuser", "somepassword");
        assertNotNull(result.getMessage(), "Should not allow login for non-existing user");
    }

    @Test
    public void testLoginWrongPassword() {
        registerUser("user13", "correctpass", "email13@example.com");
        LoginRes result = facade.loginUser("user13", "wrongpass");
        assertNotNull(result.getMessage(), "Should not allow login with incorrect password");
    }

    @Test
    public void testLogoutWithoutLoggingIn() {
        String authToken = "invalidauthtoken";
        LogoutRes result = facade.logoutUser(authToken);
        assertNotNull(result.getMessage(), "Should not allow logout with invalid auth token");
    }

    @Test
    public void testCreateExistingGame() {
        String authToken = registerUser("user14", "pass14", "email14@example.com");
        createGame("Game8", authToken);
        CreateGameRes result = facade.createGame("Game8", authToken);
        assertNotNull(result.getMessage(), "Should not allow creating a game with an existing name");
    }

    @Test
    public void testCreateGameWithoutLoggingIn() {
        String authToken = "invalidauthtoken";
        CreateGameRes result = facade.createGame("Game9", authToken);
        assertNotNull(result.getMessage(), "Should not allow creating a game with invalid auth token");
    }

    @Test
    public void testListNoGames() {
        String authToken = registerUser("user15", "pass15", "email15@example.com");
        ListGamesRes result = facade.listGames(authToken);
        assertNull(result.getMessage(), "List games failed: " + result.getMessage());
        assertEquals(0, result.getGames().size(), "Should have no games in the list");
    }

    @Test
    public void testJoinFullWhite() {
        String authToken1 = registerUser("user16", "pass16", "email16@example.com");
        String authToken2 = registerUser("user17", "pass17", "email17@example.com");
        int gameId = createGame("Game10", authToken1);
        facade.joinGame(gameId, "WHITE", authToken1);
        JoinGameRes result = facade.joinGame(gameId, "WHITE", authToken2);
        assertNotNull(result.getMessage(), "Should not allow joining a full WHITE seat");
    }

    @Test
    public void testJoinFullBlack() {
        String authToken1 = registerUser("user18", "pass18", "email18@example.com");
        String authToken2 = registerUser("user19", "pass19", "email19@example.com");
        int gameId = createGame("Game11", authToken1);
        facade.joinGame(gameId, "BLACK", authToken1);
        JoinGameRes result = facade.joinGame(gameId, "BLACK", authToken2);
        assertNotNull(result.getMessage(), "Should not allow joining a full BLACK seat");
    }

    // New Tests

    @Test
    public void testJoinNonExistingGame() {
        String authToken = registerUser("user20", "pass20", "email20@example.com");
        JoinGameRes result = facade.joinGame(9999, "WHITE", authToken);
        assertNotNull(result.getMessage(), "Should not allow joining a non-existing game");
    }

    @Test
    public void testJoinGameWithInvalidAuthToken() {
        String authToken = registerUser("user21", "pass21", "email21@example.com");
        int gameId = createGame("Game12", authToken);
        String invalidAuthToken = "invalidauthtoken";
        JoinGameRes result = facade.joinGame(gameId, "WHITE", invalidAuthToken);
        assertNotNull(result.getMessage(), "Should not allow joining a game with invalid auth token");
    }

    @Test
    public void testListGamesWithInvalidAuthToken() {
        String authToken = "invalidauthtoken";
        ListGamesRes result = facade.listGames(authToken);
        assertNotNull(result.getMessage(), "Should not allow listing games with invalid auth token");
    }

    @Test
    public void testCreateGameWithNullName() {
        String authToken = registerUser("user22", "pass22", "email22@example.com");
        CreateGameRes result = facade.createGame(null, authToken);
        assertNotNull(result.getMessage(), "Should not allow creating a game with null name");
    }


    @Test
    public void testJoinGameAlreadyFull() {
        String authToken1 = registerUser("user24", "pass24", "email24@example.com");
        String authToken2 = registerUser("user25", "pass25", "email25@example.com");
        String authToken3 = registerUser("user26", "pass26", "email26@example.com");
        int gameId = createGame("Game13", authToken1);
        facade.joinGame(gameId, "WHITE", authToken1);
        facade.joinGame(gameId, "BLACK", authToken2);
        JoinGameRes result = facade.joinGame(gameId, "WHITE", authToken3);
        assertNotNull(result.getMessage(), "Should not allow joining a game that's already full");
    }

    @Test
    public void testCreateGameWithExistingName() {
        String authToken = registerUser("user27", "pass27", "email27@example.com");
        createGame("Game14", authToken);
        CreateGameRes result = facade.createGame("Game14", authToken);
        assertNotNull(result.getMessage(), "Should not allow creating a game with an existing name");
    }

    @Test
    public void testLogoutAfterLogout() {
        String authToken = registerUser("user28", "pass28", "email28@example.com");
        logoutUser(authToken);
        LogoutRes result = facade.logoutUser(authToken);
        assertNotNull(result.getMessage(), "Should not allow logout after already logged out");
    }

}