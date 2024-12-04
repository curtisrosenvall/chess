package server;
import com.google.gson.Gson;
import dataaccess.*;
import handlers.*;
import models.AuthData;
import models.GameData;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import spark.*;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import java.util.ArrayList;


public class Server {

    Database database;

    public Server(){
        database = new Database();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/ws", Server.class);

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

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        System.out.printf("Received: %s\n", message);
        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
            AuthData auth = database.getAuth(command.getAuthString());
            String username = auth.username();
            switch (command.getCommandType()) {
                case CONNECT -> {
                    Connect connectCommand = new Gson().fromJson(message, Connect.class);
                    connect(session, username, connectCommand);
                }
                case MAKE_MOVE -> {
                    MakeMove moveCommand = new Gson().fromJson(message, MakeMove.class);
                    // Implement moveCommand handling here
                }
                case LEAVE -> {
                    Leave leaveCommand = new Gson().fromJson(message, Leave.class);
                    // Implement leaveCommand handling here
                }
                case RESIGN -> {
                    Resign resignCommand = new Gson().fromJson(message, Resign.class);
                    // Implement resign handling here
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            sendMessage(session, new ErrorMessage("Error: " + ex.getMessage()));
        }
    }

    public void sendMessage(Session session, ServerMessage message) {
        try {
            String json = new Gson().toJson(message);
            session.getRemote().sendString(json);
        } catch(Exception ex) {
            System.out.println("Error when trying to send message: " + message);
        }
    }

    public void notifySessions(ArrayList<Session> sessionList, Session rootUser, ServerMessage message, String notifyWay) {
        switch (notifyWay) {
            case "ROOT" -> sendMessage(rootUser.getRemote(), message);
            case "NOT_ROOT" -> {
                for (Session session : sessionList) {
                    if (!session.equals(rootUser))
                        sendMessage(session.getRemote(), message);
                }
            }
            case "ALL" -> {
                for (Session session : sessionList) {
                    sendMessage(session.getRemote(), message);
                }
            }
        }
    }

    public void connect(Session session, String username, Connect command) {
        database.addSession(command.getGameID(), session);
        String color;
        try {
            GameData game = database.getGame(command.getGameID());

            if (game.whiteUsername() != null && game.whiteUsername().equals(username))
                color = "White";
            else if (game.blackUsername() != null && game.blackUsername().equals(username))
                color = "Black";
            else
                color = "an Observer";
        } catch (Exception ex) {
            sendMessage(session, new ErrorMessage("Error: " + ex.getMessage()));
            return;
        }
        try {
            notifySessions(
                    database.getSessionList(command.getGameID()),
                    session,
                    new LoadGame(database.getGame(command.getGameID())),
                    "ROOT"
            );
            notifySessions(
                    database.getSessionList(command.getGameID()),
                    session,
                    new Notification(username + " joined the game as " + color),
                    "NOT_ROOT"
            );
        } catch (Exception ex) {
            System.out.println("Error trying to get the database");
        }
    }

}