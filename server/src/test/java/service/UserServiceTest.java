package service;

import dataaccess.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.*;
import result.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for UserService.
 */
public class UserServiceTest extends ParentTest {

    private UserService service;
    private Database db;

    @BeforeEach
    public void setUp() {
        db = new Database();
        service = new UserService(db);
    }

    @Test
    public void testCreateUserAndCorrectLogin() {
        RegisterReq registerRequest = new RegisterReq("Test", "1234", "test@gmail.com");
        LoginReq loginRequest = new LoginReq("Test", "1234");

        RegisterRes registerResult = service.createUser(registerRequest);
        LoginRes loginResult = service.loginUser(loginRequest);

        assertTrue(registerResult.isSuccess(), registerResult.getMessage());
        assertTrue(loginResult.isSuccess(), loginResult.getMessage());
    }

    @Test
    public void testCreateUserAndWrongPasswordLogin() {
        RegisterReq registerRequest = new RegisterReq("Test", "1234", "test@gmail.com");
        LoginReq loginRequest = new LoginReq("Test", "5678");

        RegisterRes registerResult = service.createUser(registerRequest);
        LoginRes loginResult = service.loginUser(loginRequest);

        assertTrue(registerResult.isSuccess(), registerResult.getMessage());
        assertFalse(loginResult.isSuccess(), loginResult.getMessage());
    }

    @Test
    public void testLoginWithoutRegistration() {
        LoginReq loginRequest = new LoginReq("Test", "1234");
        LoginRes loginResult = service.loginUser(loginRequest);

        assertFalse(loginResult.isSuccess(), loginResult.getMessage());
    }

    @Test
    public void testCreateLoginAndLogout() {
        RegisterReq registerRequest = new RegisterReq("Test", "1234", "test@gmail.com");
        LoginReq loginRequest = new LoginReq("Test", "1234");

        RegisterRes registerResult = service.createUser(registerRequest);
        LoginRes loginResult = service.loginUser(loginRequest);
        LogoutReq logoutRequest = new LogoutReq(loginResult.getAuthToken());
        LogoutRes logoutResult = service.logoutUser(logoutRequest);

        assertTrue(registerResult.isSuccess(), registerResult.getMessage());
        assertTrue(loginResult.isSuccess(), loginResult.getMessage());
        assertTrue(logoutResult.isSuccess(), logoutResult.getMessage());
    }

    @Test
    public void testLogoutWithoutLogin() {
        LogoutReq logoutRequest = new LogoutReq("invalidAuthToken");
        LogoutRes logoutResult = service.logoutUser(logoutRequest);

        assertFalse(logoutResult.isSuccess(), logoutResult.getMessage());
    }

    @Test
    public void testCreateUserAndLogoutWithoutLogin() {
        RegisterReq registerRequest = new RegisterReq("Test", "1234", "test@gmail.com");
        RegisterRes registerResult = service.createUser(registerRequest);
        LogoutReq logoutRequest = new LogoutReq(registerResult.getAuthToken());
        LogoutRes logoutResult = service.logoutUser(logoutRequest);

        assertTrue(registerResult.isSuccess(), registerResult.getMessage());
        assertTrue(logoutResult.isSuccess(), logoutResult.getMessage());
    }
}