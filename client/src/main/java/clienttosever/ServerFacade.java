package clienttosever;

import com.google.gson.Gson;
import request.*;
import result.*;

import java.io.InputStreamReader;

public class ServerFacade {

    ClientCommunicator communicator;

    public ServerFacade(int port) {
        communicator = new ClientCommunicator(port);
    }

    public RegisterResult registerUser(String username, String password, String email) {
        RegisterRequest request = new RegisterRequest(username, password, email);
        URLClientStrings clientStrings = new URLClientStrings("/user", "POST", "");
        InputStreamReader reader = communicator.clientToServer(request, clientStrings);

        // Debug statement to check if the reader is null
        if (reader == null) {
            System.err.println("Error: InputStreamReader is null in registerUser");
            return new RegisterResult(false, "Error: Unable to communicate with the server.");
        }

        return new Gson().fromJson(reader, RegisterResult.class);
    }

    public LoginResult loginUser(String username, String password) {
        LoginRequest request = new LoginRequest(username, password);
        URLClientStrings clientString = new URLClientStrings("/session", "POST", "");
        InputStreamReader reader = communicator.clientToServer(request, clientString);
        return new Gson().fromJson(reader, LoginResult.class);
    }

    public LogoutResult logoutUser(String authToken) {
        LogoutRequest request = new LogoutRequest(authToken);
        URLClientStrings clientStrings = new URLClientStrings("/session", "DELETE", authToken);
        InputStreamReader reader = communicator.clientToServer(request, clientStrings);
        return new Gson().fromJson(reader, LogoutResult.class);
    }

    public CreateGameResult createGame(String gameName, String authToken) {
        CreateGameRequest request = new CreateGameRequest(authToken, gameName);
        URLClientStrings clientStrings = new URLClientStrings("/game", "POST", authToken);
        InputStreamReader reader = communicator.clientToServer(request, clientStrings);
        return new Gson().fromJson(reader, CreateGameResult.class);
    }

    public ListGamesResult listGames(String authToken) {
        ListGamesRequest request = new ListGamesRequest(authToken);
        URLClientStrings clientStrings = new URLClientStrings("/game", "GET", authToken);
        InputStreamReader reader = communicator.clientToServer(request, clientStrings);
        return new Gson().fromJson(reader, ListGamesResult.class);
    }

    public JoinGameResult joinGame(Integer gameID, String playerColor, String authToken) {
        JoinGameRequest request = new JoinGameRequest(authToken, playerColor, gameID);
        URLClientStrings clientStrings = new URLClientStrings("/game", "PUT", authToken);
        InputStreamReader reader = communicator.clientToServer(request, clientStrings);
        return new Gson().fromJson(reader, JoinGameResult.class);
    }

    public ClearResult clear() {
        ClearRequest request = new ClearRequest();
        URLClientStrings clientStrings = new URLClientStrings("/db", "DELETE", "");
        InputStreamReader reader = communicator.clientToServer(request, clientStrings);
        return new Gson().fromJson(reader, ClearResult.class);
    }
}