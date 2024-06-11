package Clienter2Sever;

import com.google.gson.Gson;
import request.*;
import result.*;

import java.io.InputStreamReader;

public class ServerFacade {

    ClientCommunicator clientCommunicator;

    public ServerFacade(int port) {
        clientCommunicator = new ClientCommunicator(port);
    }

    public RegisterResult registerUser(String username, String password, String email) {
        RegisterRequest request = new RegisterRequest(username, password, email);
        URLStrings clientStrings = new URLStrings("/user", "POST", "");
        InputStreamReader reader = clientCommunicator.client2Server(request,clientStrings);
        return new Gson().fromJson(reader, RegisterResult.class);
    }

    public LoginResult loginUser(String username, String password) {
        LoginRequest request = new LoginRequest(username, password);
        URLStrings clientStrings = new URLStrings("/session", "POST", "");
        InputStreamReader reader = clientCommunicator.client2Server(request,clientStrings);
        return new Gson().fromJson(reader, LoginResult.class);
    }

    public LogoutResult logoutUser(String token) {
        LogoutRequest request = new LogoutRequest(token);
        URLStrings clientStrings = new URLStrings("/session", "DELETE", token);
        InputStreamReader reader = clientCommunicator.client2Server(request,clientStrings);
        return new Gson().fromJson(reader, LogoutResult.class);
    }

    public CreateGameResult createGame(String gameName, String token) {
        CreateGameRequest request = new CreateGameRequest(token, gameName);
        URLStrings clientStrings = new URLStrings("/game", "POST", token);
        InputStreamReader reader = clientCommunicator.client2Server(request,clientStrings);
        return new Gson().fromJson(reader, CreateGameResult.class);
    }

    public ListGamesResult listGames(String token) {
        ListGamesRequest request = new ListGamesRequest(token);
        URLStrings clientStrings = new URLStrings("/game", "GET", token);
        InputStreamReader reader = clientCommunicator.client2Server(request, clientStrings);
        return new Gson().fromJson(reader, ListGamesResult.class);
    }

    public JoinGameResult joinGame(Integer gameID, String playerColor, String token) {
        JoinGameRequest request = new JoinGameRequest(token, playerColor, gameID);
        URLStrings clientStrings = new URLStrings("/game", "PUT", token);
        InputStreamReader reader = clientCommunicator.client2Server(request,clientStrings);
        return new Gson().fromJson(reader, JoinGameResult.class);
    }

    public ClearRequest clearGame(Integer gameID, String token) {
        ClearRequest request = new ClearRequest();
        URLStrings clientStrings = new URLStrings("/db", "DELETE", "");
        InputStreamReader reader = clientCommunicator.client2Server(request,clientStrings);
        return new Gson().fromJson(reader, ClearRequest.class);
    }
}
