package server;
import dataaccess.Database;
import handler.ClearAll;
import handler.CreateGame;
import handler.JoinGame;
import spark.*;

public class Server {

    Database database;

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        database = new Database();


        // Register your endpoints and handle exceptions here.
        Spark.post("/game", (req, res) -> (new CreateGame(database)).handle(req, res));
        Spark.put("/game", (req, res) -> (new JoinGame(database)).handle(req, res));
        Spark.delete("/db", (req, res) -> (new ClearAll(database)).handle(req, res));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
