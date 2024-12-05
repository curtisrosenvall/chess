package server;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import com.google.gson.Gson;
import dataaccess.*;
import handlers.*;
import models.AuthData;
import models.GameData;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
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

@WebSocket
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
                    makeMove(session, username, moveCommand);
                }
                case LEAVE -> {
                    Leave leaveCommand = new Gson().fromJson(message, Leave.class);
                    leaveGame(session, username, leaveCommand);
                }
                case RESIGN -> {
                    Resign resignCommand = new Gson().fromJson(message, Resign.class);
                    resign(session, username, resignCommand);
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
            System.out.println("Sent message to session: " + session);
        } catch (Exception ex) {
            System.out.println("Error when trying to send message to session: " + session);
            ex.printStackTrace();
        }
    }

    public void notifySessions(ArrayList<Session> sessionList, Session rootUser, ServerMessage message, String notifyWay) {
        switch (notifyWay) {
            case "ROOT":
                sendMessage(rootUser, message);
                break;
            case "NOT_ROOT":
                for (Session session : sessionList) {
                    if (!session.equals(rootUser))
                        sendMessage(session, message);
                }
                break;
            case "ALL":
                for (Session session : sessionList) {
                    sendMessage(session, message);
                }
                break;
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
                    "ALL"
            );
        } catch (Exception ex) {
            System.out.println("Error trying to get the database");
        }
    }

    public void makeMove(Session session, String username, MakeMove command) {
        try {
            ChessMove move = command.getMove();
            String stringMove = getStringVersion(move);
            if (stringMove == null) {
                sendMessage(session, new ErrorMessage("Invalid move"));
                return;
            }
            GameData game = database.getGame(command.getGameID());
            ChessGame.TeamColor color = game.game().getTeamTurn();
            if (!username.equals(game.whiteUsername()) && !username.equals(game.blackUsername())) {
                sendMessage(session, new ErrorMessage("You cannot move pieces as the observer"));
                return;
            }
            ChessGame.TeamColor playerColor = (game.whiteUsername().equals(username))
                    ? ChessGame.TeamColor.WHITE
                    : ChessGame.TeamColor.BLACK;
            if (game.game().getBoard().getPiece(move.getStartPosition()).getTeamColor() != playerColor) {
                sendMessage(session, new ErrorMessage("You cannot move the other team's pieces"));
                return;
            }
            game.game().makeMove(move);
            database.updateGame(game);

            ArrayList<Session> sessionList = database.getSessionList(command.getGameID());

            notifySessions(sessionList, session, new LoadGame(game), "ALL");
            notifySessions(sessionList, session, new Notification(username + " has made a move: " + stringMove), "ALL");
            if (game.game().isInCheckmate(game.game().getTeamTurn())) {
                notifySessions(sessionList, session,
                        new Notification(username + " has Checkmated " + ((color == ChessGame.TeamColor.WHITE)
                                ? game.blackUsername()
                                : game.whiteUsername())),
                        "ALL");
            } else if (game.game().isInStalemate(game.game().getTeamTurn())) {
                notifySessions(sessionList, session,
                        new Notification(((color == ChessGame.TeamColor.WHITE)
                                ? game.blackUsername()
                                : game.whiteUsername()) + " is in Stalemate"),
                        "ALL");
            } else if (game.game().isInCheck(game.game().getTeamTurn())) {
                notifySessions(sessionList, session,
                        new Notification(username + " has Checked " + ((color == ChessGame.TeamColor.WHITE)
                                ? game.blackUsername()
                                : game.whiteUsername())),
                        "ALL");
            }
        } catch (Exception ex) {
            sendMessage(session, new ErrorMessage("Error: " + ex.getMessage()));
        }
    }

    public void leaveGame(Session session, String username, Leave command) {
        database.removeSession(command.getGameID(), session);
        try {
            GameData game = database.getGame(command.getGameID());
            GameData newGame;
            if((game.blackUsername() != null) && game.blackUsername().equals(username))
                newGame = new GameData(game.gameID(), game.whiteUsername(), null, game.gameName(), game.game());
            else if((game.whiteUsername() != null) && game.whiteUsername().equals(username))
                newGame = new GameData(game.gameID(), null, game.blackUsername(), game.gameName(), game.game());
            else
                newGame = game;
            database.updateGame(newGame);
            notifySessions(database.getSessionList(command.getGameID()), session, new Notification(username + " has left the game"), "NOT_ROOT");
        } catch(Exception ex) {
            System.out.println("NOT IN GAME!!!");
        }
    }

    public void resign(Session session, String username, Resign command) {
        try {
            GameData game = database.getGame(command.getGameID());
            System.out.println("Processing resign request from user: " + username);

            // Existing checks...

            System.out.println("Setting game as over...");
            ChessGame resignGame = game.game();
            resignGame.setGameOver(true);
            GameData newGame = new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), resignGame);

            System.out.println("Updating game in database...");
            database.updateGame(newGame);
            System.out.println("Game updated successfully.");

            ArrayList<Session> sessionList = database.getSessionList(command.getGameID());

            System.out.println("Sending resignation notification...");
            notifySessions(sessionList, session, new Notification(username + " has resigned the game"), "NOT_ROOT");
            System.out.println("Resignation notification sent.");

        } catch (Exception ex) {
            System.out.println("Failed to resign");
            ex.printStackTrace();
        }
    }

    public String getStringVersion(ChessMove move) {

        String startPos = intToChar(move.getStartPosition().getColumn()) + String.valueOf(move.getStartPosition().getRow());
        String endPos = intToChar(move.getEndPosition().getColumn()) + String.valueOf(move.getEndPosition().getRow());
        String promotion = pieceToString(move.getPromotionPiece());

        return startPos + " to " + endPos + ((promotion == null) ? "" : promotion);
    }

    public Character intToChar(int col) {
        switch(col) {
            case 1 -> { return 'a'; }
            case 2 -> { return 'b'; }
            case 3 -> { return 'c'; }
            case 4 -> { return 'd'; }
            case 5 -> { return 'e'; }
            case 6 -> { return 'f'; }
            case 7 -> { return 'g'; }
            case 8 -> { return 'h'; }
            default -> { return null; }
        }
    }

    public String pieceToString(ChessPiece.PieceType piece) {
        switch(piece) {
            case null -> { return null; }
            case QUEEN -> { return "promoting to a Queen"; }
            case ROOK -> { return "promoting to a Rook"; }
            case BISHOP -> { return "promoting to a Bishop"; }
            case KNIGHT -> { return "promoting to a Knight"; }
            default -> { return null; }
        }
    }


}