package dataaccess;

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

    @Test
    public void regiserUser() {
        try{
            database.createUser("name","password", "email");
            Assertions.assertNotNull(database.getUser("name"));
            Assertions.assertEquals(1,database.userDataBase.size());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }
}
