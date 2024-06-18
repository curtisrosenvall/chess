package server;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import com.google.gson.Gson;
import dataaccess.*;
import handler.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import spark.*;
import websocket.commands.*;
import websocket.messages.*;

import java.util.ArrayList;

@WebSocket
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

    public AuthData getUsername(String authToken) {
        try {
            return database.getAuth(authToken);
        } catch(Exception ex) {
            System.out.println("ERROR! CAN'T GET AUTH!");
            return null;
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.printf("Received: %s\n", message);
        //session.getRemote().sendString("WebSocket response: " + message);

        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

            // Throws a custom UnauthorizedException. Yours may work differently.
            AuthData auth = database.getAuth(command.getAuthString());
            String username = auth.username();

            //This implements a map that organizes all the sessions. GameID to an arraylist of all the authTokens
            //Put this inside of the connect method as this is the only time info is added into the map
            //saveSession(command.getGameID(), session);

            switch (command.getCommandType()) {
                //Adds the authToken into the map of sessions.
                case CONNECT -> {
                    ConnectCommand connectCommand = new Gson().fromJson(message, ConnectCommand.class);
                    connect(session, username, connectCommand);
                }
                case MAKE_MOVE -> {
                    MakeMoveCommand moveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
                    makeMove(session, username, moveCommand);
                }
                case LEAVE -> {
                    LeaveGameCommand leaveCommand = new Gson().fromJson(message, LeaveGameCommand.class);
                    leaveGame(session, username, leaveCommand);
                }
                //Doesn't remove anyone, just sends a message
                case RESIGN -> {
                    ResignCommand resignCommand = new Gson().fromJson(message, ResignCommand.class);
                    resign(session, username, resignCommand);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            sendMessage(session.getRemote(), new ErrorMessage("Error: " + ex.getMessage()));
        }
    }

    public void connect(Session session, String username, ConnectCommand command) {
        database.addSession(command.getGameID(), session);
        String color;
        //Add username to database
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
        //Notify all other people
        try {
            notifySessions(database.getSessionList(command.getGameID()), session, new OnGameLoadMessage(database.getGame(command.getGameID())), "ROOT");
            notifySessions(database.getSessionList(command.getGameID()), session, new NotificationMessage(username + " joined the game as " + color), "NOT_ROOT");
        } catch(Exception ex) {
            System.out.println("Error trying to get the database");
        }
    }

    public void makeMove(Session session, String username, MakeMoveCommand command) {
        try {
            ChessMove move = command.getMove();
            String stringMove = getStringVersion(move);
            if(stringMove == null) {
                sendMessage(session.getRemote(), new ErrorMessage("Invalid move"));
                return;
            }
            GameData game = database.getGame(command.getGameID());
            ChessGame.TeamColor color = game.game().getTeamTurn();
            if(!username.equals(game.whiteUsername()) && !username.equals(game.blackUsername())) {
                sendMessage(session.getRemote(), new ErrorMessage("You cannot move pieces as the observer"));
                return;
            }
            ChessGame.TeamColor playerColor = ((game.whiteUsername().equals(username)) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK);
            if(game.game().getBoard().getPiece(move.getStartPosition()).getTeamColor() != playerColor) {
                sendMessage(session.getRemote(), new ErrorMessage("You cannot move the other team's pieces"));
                return;
            }
            game.game().makeMove(move);
            database.updateGame(game);

            //LOAD_GAME Message
            notifySessions(database.getSessionList(command.getGameID()), session, new OnGameLoadMessage(game), "ALL");
            //Notify a move has been made
            notifySessions(database.getSessionList(command.getGameID()), session, new NotificationMessage(username + " has made a move: " + stringMove), "NOT_ROOT");
            if(game.game().isInCheckmate(game.game().getTeamTurn())) {
                notifySessions(database.getSessionList(command.getGameID()), session, new NotificationMessage(username + " has Checkmated " + ((color == ChessGame.TeamColor.WHITE) ? game.blackUsername() : game.whiteUsername())), "ALL");
            } else if(game.game().isInStalemate(game.game().getTeamTurn())) {
                notifySessions(database.getSessionList(command.getGameID()), session, new NotificationMessage(((color == ChessGame.TeamColor.WHITE) ? game.blackUsername() : game.whiteUsername()) + " is in Stalemate"), "ALL");
            } else if(game.game().isInCheck(game.game().getTeamTurn())) {
                notifySessions(database.getSessionList(command.getGameID()), session, new NotificationMessage(username + " has Checked " + ((color == ChessGame.TeamColor.WHITE) ? game.blackUsername() : game.whiteUsername())), "ALL");
            }
        } catch(Exception ex) {
            sendMessage(session.getRemote(), new ErrorMessage("Error: " + ex.getMessage()));
        }
    }

    public void leaveGame(Session session, String username, LeaveGameCommand command) {
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
            notifySessions(database.getSessionList(command.getGameID()), session, new NotificationMessage(username + " has left the game"), "NOT_ROOT");
        } catch(Exception ex) {
            System.out.println("NOT IN GAME!!!");
        }
        //sendMessage

    }

    public void resign(Session session, String username, ResignCommand command) {
        try {
            GameData game = database.getGame(command.getGameID());
            if(!username.equals(game.whiteUsername()) && !username.equals(game.blackUsername())) {
                sendMessage(session.getRemote(), new ErrorMessage("You can't resign as the observer"));
                return;
            }
            if(game.game().isGameOver()) {
                sendMessage(session.getRemote(), new ErrorMessage("The game is over, no more moves or resignations can be made"));
                return;
            }
            ChessGame resignGame = game.game();
            resignGame.setGameOver(true);
            GameData newGame = new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), resignGame);
            database.updateGame(newGame);
            notifySessions(database.getSessionList(command.getGameID()), session, new NotificationMessage(username + " has resigned the game"), "ALL");
        } catch(Exception ex) {
            System.out.println("Failed to resign");
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