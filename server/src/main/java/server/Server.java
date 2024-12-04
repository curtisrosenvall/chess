package server;
import com.google.gson.Gson;
import dataaccess.*;
import handlers.*;
import models.AuthData;
import models.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import spark.Spark;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import java.util.ArrayList;

import static javax.management.remote.JMXConnectorFactory.connect;

public class Server {

    Database database;

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/ws", Server.class);
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

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.printf("Received: %s\n", message);
        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
            AuthData auth = database.getAuth(command.getAuthString());
            String username = auth.username();
            switch (command.getCommandType()) {
                case CONNECT -> {
                    Connect connectCommand = new Gson().fromJson(message, Connect.class);
                    connectCommand(session, username, connectCommand);
                }
                case MAKE_MOVE -> {
                    MakeMove moveCommand = new Gson().fromJson(message, MakeMove.class);
//                    moveCommand(session, username, moveCommand);
                }
                case LEAVE -> {
                    Leave leaveCommand = new Gson().fromJson(message, Leave.class);
//                    leaveCommand(session, username, leaveCommand);
                }
                case RESIGN -> {
                    Resign resignCommand = new Gson().fromJson(message, Resign.class);
//                    resign(session, username, resignCommand);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendMessage(RemoteEndpoint endpoint, ServerMessage message) {
        try {
            String json = new Gson().toJson(message);
            endpoint.sendString(json);
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

            if((game.whiteUsername() != null ) && game.whiteUsername().equals(username))
                color = "White";
            else if((game.blackUsername() != null) && game.blackUsername().equals(username))
                color = "Black";
            else
                color = "an Observer";
        } catch(Exception ex) {
            sendMessage(session.getRemote(), new ErrorMessage("Error: " + ex.getMessage()));
            return;
        }
        try {
            notifySessions(database.getSessionList(command.getGameID()), session, new LoadGame(database.getGame(command.getGameID())), "ROOT");
            notifySessions(database.getSessionList(command.getGameID()), session, new Notification(username + " joined the game as " + color), "NOT_ROOT");
        } catch(Exception ex) {
            System.out.println("Error trying to get the database");
        }
    }

}