package client;

import org.junit.jupiter.api.*;
import result.*;
import result.RegisterRes;
import server.Server;
import facade.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static String authToken;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    void clear(){
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }


    @Test
    public void registerUser(){
        RegisterRes registerResult = facade.registerUser("username", "password", "email");
        Assertions.assertNull(registerResult.getMessage());
        authToken = registerResult.getAuthToken();
    }

    @Test
    public void loginUser() {
        logoutUser();
        LoginRes result = facade.loginUser("username", "password");
        Assertions.assertNull(result.getMessage());
        authToken = result.getAuthToken();
    }

    @Test
    public void logoutUser() {
        registerUser();
        LogoutRes result = facade.logoutUser(authToken);
        Assertions.assertNull(result.getMessage());
    }

    @Test
    public void registerMultipleUsers() {
        registerUser();
        RegisterRes result1 = facade.registerUser("test", "1234", "test@gmail.com");
        Assertions.assertNull(result1.getMessage());
        RegisterRes result2 = facade.registerUser("bob", "secret", "bob@gmail.com");
        Assertions.assertNull(result2.getMessage());
    }



}
