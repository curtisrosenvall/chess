package clienttoserver;

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
        URLStrings clientStrings = new URLStrings("/user", "POST", "");
        InputStreamReader reader = communicator.clientToServer(request, clientStrings);
        return new Gson().fromJson(reader, RegisterResult.class);
    }

    public LoginResult loginUser(String username, String password) {
        LoginRequest request = new LoginRequest(username, password);
        URLStrings clientString = new URLStrings("/session", "POST", "");
        InputStreamReader reader = communicator.clientToServer(request, clientString);
        return new Gson().fromJson(reader, LoginResult.class);
    }

    public LogoutResult logoutUser(String authToken) {
        LogoutRequest request = new LogoutRequest(authToken);
        URLStrings clientStrings = new URLStrings("/session", "DELETE", authToken);
        InputStreamReader reader = communicator.clientToServer(request, clientStrings);
        return new Gson().fromJson(reader, LogoutResult.class);
    }

    public CreateGameResult createGame(String gameName, String authToken) {
        CreateGameRequest request = new CreateGameRequest(authToken, gameName);
        URLStrings clientStrings = new URLStrings("/game", "POST", authToken);
        InputStreamReader reader = communicator.clientToServer(request, clientStrings);
        return new Gson().fromJson(reader, CreateGameResult.class);
    }

    public ListGamesResult listGames(String authToken) {
        ListGamesRequest request = new ListGamesRequest(authToken);
        URLStrings clientStrings = new URLStrings("/game", "GET", authToken);
        InputStreamReader reader = communicator.clientToServer(request, clientStrings);
        return new Gson().fromJson(reader, ListGamesResult.class);
    }

    public JoinGameResult joinGame(Integer gameID, String playerColor, String authToken) {
        JoinGameRequest request = new JoinGameRequest(authToken, playerColor, gameID);
        URLStrings clientStrings = new URLStrings("/game", "PUT", authToken);
        InputStreamReader reader = communicator.clientToServer(request, clientStrings);
        return new Gson().fromJson(reader, JoinGameResult.class);
    }

    public ClearResult clear() {
        ClearRequest request = new ClearRequest();
        URLStrings clientStrings = new URLStrings("/db", "DELETE", "");
        InputStreamReader reader = communicator.clientToServer(request, clientStrings);
        return new Gson().fromJson(reader, ClearResult.class);
    }
}