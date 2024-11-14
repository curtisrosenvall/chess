package facade;
import com.google.gson.Gson;
import request.*;
import result.*;

import java.io.InputStreamReader;

public class ServerFacade {
    ClientInputReader clientReader;

    public ServerFacade(int port){
        clientReader = new ClientInputReader(port);
    }

    public RegisterRes registerUser(String username, String password, String email){
        System.out.println("[REGISTERING_USER]"+ "="  + username);
        RegisterReq request = new RegisterReq(username,password,email);
        Url clientUrl = new Url("/user", "POST", "");
        InputStreamReader reader = clientReader.clientToServer(request,clientUrl);
        if(reader == null){
            System.out.println("[FAILED] User was not registered: " + username);
        }
        return new Gson().fromJson(reader,RegisterRes.class);
    }

    public LoginRes loginUser(String username, String password){
        System.out.println("[LOGGING_IN....]: " + username);
        LoginReq request = new LoginReq(username,password);
        Url clientUrl = new Url("/session","POST","");
        InputStreamReader reader = clientReader.clientToServer(request,clientUrl);
        if(reader == null){
            System.out.println("[FAILED]User was not logged in: " + username);
        }
        return new Gson().fromJson(reader,LoginRes.class);
    }

    public LogoutRes logoutUser(String authToken){
        System.out.println("[LOGGING_OUT]: " + authToken);
        LogoutReq request = new LogoutReq(authToken);
        Url clientUrl = new Url("/session", "DELETE", authToken);
        InputStreamReader reader = clientReader.clientToServer(request, clientUrl);
        if (reader == null) {
            System.out.println("Failed to logout user with authToken: " + authToken);
        }
        return new Gson().fromJson(reader, LogoutRes.class);
    }

//    once loggedIN

    public CreateGameRes createGame(String gameName, String authToken){
        System.out.println("[CREATING_GAME...]: " + gameName);
        CreateGameReq request = new CreateGameReq(authToken, gameName);
        Url clientUrl = new Url("/game", "POST", authToken);
        InputStreamReader reader = clientReader.clientToServer(request, clientUrl);
        if (reader == null) {
            System.out.println("Failed to create game: " + gameName);
        }
        return new Gson().fromJson(reader, CreateGameRes.class);
    }

    public ListGamesRes listGames(String authToken){
        System.out.println("[LISTING_GAMES..]" + " for authToken: " + authToken);
        ListGamesReq request = new ListGamesReq(authToken);
        Url clientUrl = new Url("/game", "GET", authToken);
        InputStreamReader reader = clientReader.clientToServer(request, clientUrl);
        if (reader == null) {
            System.out.println("Failed to list games for authToken: " + authToken);

        }
        return new Gson().fromJson(reader, ListGamesRes.class);
    }

    public JoinGameRes joinGame(Integer gameID, String playerColor, String authToken){
        System.out.println("[JOINING_GAME...]: " + gameID + " as " + playerColor);
        JoinGameReq request = new JoinGameReq(authToken, playerColor, gameID);
        Url clientUrl = new Url("/game", "PUT", authToken);
        InputStreamReader reader = clientReader.clientToServer(request, clientUrl);
        if (reader == null) {
            System.out.println("Failed to join game: " + gameID);
            return new JoinGameRes(null, "Failed to connect to server.");
        }
        return new Gson().fromJson(reader, JoinGameRes.class);
    }

    public ClearRes clear(){
        ClearReq request = new ClearReq();
        Url clientUrl = new Url("/db", "DELETE", "");
        InputStreamReader reader = clientReader.clientToServer(request, clientUrl);
        return new Gson().fromJson(reader, ClearRes.class);
    }
}
