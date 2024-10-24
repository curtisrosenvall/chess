package server;
import dataaccess.*;
import handlers.*;
import spark.*;

public class Server {

    Database database;

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        //Create DAOs to pass through
        database = new Database();

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (req, res) -> (new Register(database)).handle(req,res));
        Spark.post("/session", (req, res) -> (new Login(database)).handle(req,res));
        Spark.delete("/session", (req, res) -> (new Logout(database)).handle(req, res));
        Spark.get("/game", (req, res) -> (new ListGames(database)).handle(req, res));
        Spark.post("/game", (req, res) -> (new CreateGame(database)).handle(req, res));
        Spark.put("/game", (req, res) -> (new JoinGame(database)).handle(req, res));
        Spark.delete("/db", (req, res) -> (new Clear(database)).handle(req, res));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}