package clienttoserver;

import com.google.gson.Gson;
import request.*;
import result.*;

import java.io.InputStreamReader;

public class ServerFacade {

    private final ClientCommunicator communicator;

    public ServerFacade(int port) {
        communicator = new ClientCommunicator(port);
    }

    public RegisterResult registerUser(String username, String password, String email) {
        System.out.println("Registering user: " + username);
        RegisterRequest request = new RegisterRequest(username, password, email);
        URLStrings clientStrings = new URLStrings("/user", "POST", "");
        InputStreamReader reader = communicator.clientToServer(request, clientStrings);
        if (reader == null) {
            System.out.println("Failed to register user: " + username);
        }
        return new Gson().fromJson(reader, RegisterResult.class);
    }

    public LoginResult loginUser(String username, String password) {
        System.out.println("Logging in user: " + username);
        LoginRequest request = new LoginRequest(username, password);
        URLStrings clientStrings = new URLStrings("/session", "POST", "");
        InputStreamReader reader = communicator.clientToServer(request, clientStrings);
        if (reader == null) {
            System.out.println("Failed to login user: " + username);

        }
        return new Gson().fromJson(reader, LoginResult.class);
    }

    public LogoutResult logoutUser(String authToken) {
        System.out.println("Logging out user with authToken: " + authToken);
        LogoutRequest request = new LogoutRequest(authToken);
        URLStrings clientStrings = new URLStrings("/session", "DELETE", authToken);
        InputStreamReader reader = communicator.clientToServer(request, clientStrings);
        if (reader == null) {
            System.out.println("Failed to logout user with authToken: " + authToken);

        }
        return new Gson().fromJson(reader, LogoutResult.class);
    }

    public CreateGameResult createGame(String gameName, String authToken) {
        System.out.println("Creating game: " + gameName);
        CreateGameRequest request = new CreateGameRequest(authToken, gameName);
        URLStrings clientStrings = new URLStrings("/game", "POST", authToken);
        InputStreamReader reader = communicator.clientToServer(request, clientStrings);
        if (reader == null) {
            System.out.println("Failed to create game: " + gameName);

        }
        return new Gson().fromJson(reader, CreateGameResult.class);
    }

    public ListGamesResult listGames(String authToken) {
        System.out.println("Listing games for authToken: " + authToken);
        ListGamesRequest request = new ListGamesRequest(authToken);
        URLStrings clientStrings = new URLStrings("/game", "GET", authToken);
        InputStreamReader reader = communicator.clientToServer(request, clientStrings);
        if (reader == null) {
            System.out.println("Failed to list games for authToken: " + authToken);

        }
        return new Gson().fromJson(reader, ListGamesResult.class);
    }

    public JoinGameResult joinGame(Integer gameID, String playerColor, String authToken) {
        System.out.println("Joining game: " + gameID + " as " + playerColor);
        JoinGameRequest request = new JoinGameRequest(authToken, playerColor, gameID);
        URLStrings clientStrings = new URLStrings("/game", "PUT", authToken);
        InputStreamReader reader = communicator.clientToServer(request, clientStrings);
        if (reader == null) {
            System.out.println("Failed to join game: " + gameID);
            return new JoinGameResult(null, "Failed to connect to server.");
        }
        return new Gson().fromJson(reader, JoinGameResult.class);
    }

    public ClearResult clear() {
        System.out.println("Clearing database");
        ClearRequest request = new ClearRequest();
        URLStrings clientStrings = new URLStrings("/db", "DELETE", "");
        InputStreamReader reader = communicator.clientToServer(request, clientStrings);
        if (reader == null) {
            System.out.println("Failed to clear database");
        }
        return new Gson().fromJson(reader, ClearResult.class);
    }
}