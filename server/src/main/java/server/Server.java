package server;
import dataaccess.*;
import handlers.Register;
import spark.*;

public class Server {

    Database database;

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        database = new Database();

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (req, res) -> (new Register(database)).handle(req,res));

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
