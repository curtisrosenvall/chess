package service;

import dataaccess.DataAccessException;
import dataaccess.Database;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.*;
import result.*;

public class UserServiceTest extends ParentTest {

    private UserService service;
    private RegisterResult regRes;
    private LoginResult logRes;
    private LoginResult loginRes;
    private LogoutResult logoutRes;

    @BeforeEach
    public void createDataBase() {
        Database db = new Database();
        service = new UserService(db);
    }

    @Test
    public void createUserCorrectLogin() {
        regRes = service.createUser( new RegisterRequest("Test", "1234", "test@gmail.com"));
        logRes = service.loginUser(new LoginRequest("Test", "1234"));
        Assertions.assertTrue(regRes.isSuccess(), regRes.getMessage());
        Assertions.assertTrue(logRes.isSuccess(), logRes.getMessage());
    }

    @Test
    public void createUserWrongPassword() {
        regRes = service.createUser( new RegisterRequest("Test", "1234", "test@gmail.com"));
        logRes = service.loginUser( new LoginRequest("Test", "5678"));
        Assertions.assertTrue(regRes.isSuccess(), regRes.getMessage());
        Assertions.assertFalse(logRes.isSuccess(), logRes.getMessage());
    }

    @Test
    public void skipCreateTryLogin() {
        logRes = service.loginUser( new LoginRequest("Test", "1234"));
        Assertions.assertFalse(logRes.isSuccess(), logRes.getMessage());
    }


    @Test
    public void createLoginAndLogout() {
        regRes = service.createUser( new RegisterRequest("Test", "1234", "test@gmail.com"));
        Assertions.assertTrue(regRes.isSuccess(), regRes.getMessage());
        loginRes = service.loginUser(new LoginRequest("Test", "1234"));
        Assertions.assertTrue(loginRes.isSuccess(), loginRes.getMessage());
        logoutRes = service.logoutUser(new LogoutRequest(loginRes.getAuthToken()));
        Assertions.assertTrue(logoutRes.isSuccess(), logoutRes.getMessage());
    }

    @Test
    public void noCreateTryLogout() {
        logoutRes = service.logoutUser(new LogoutRequest("987654321"));
        Assertions.assertFalse(logoutRes.isSuccess(), logoutRes.getMessage());
    }

    @Test
    public void createNoLoginTryLogout() {
        regRes = service.createUser( new RegisterRequest("Test", "1234", "test@gmail.com"));
        Assertions.assertTrue(regRes.isSuccess(), regRes.getMessage());
        logoutRes = service.logoutUser(new LogoutRequest(regRes.getAuthToken()));
        Assertions.assertTrue(logoutRes.isSuccess(), logoutRes.getMessage());
    }
}
