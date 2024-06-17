package server;

import dataaccess.*;
import handler.*;
import spark.*;

public class Server {

    Database database;

    public Server() {
        database = new Database();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/ws", Server.class);

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (req, res) -> (new Register(database)).handle(req,res));
        Spark.post("/session", (req, res) -> (new Login(database)).handle(req,res));
        Spark.delete("/session", (req, res) -> (new Logout(database)).handle(req, res));
        Spark.get("/game", (req, res) -> (new ListGames(database)).handle(req, res));
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