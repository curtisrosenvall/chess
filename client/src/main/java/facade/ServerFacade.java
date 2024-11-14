package facade;
import com.google.gson.Gson;
import request.CreateGameReq;
import request.LoginReq;
import request.LogoutReq;
import request.RegisterReq;
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
        System.out.println("Logging out user with authToken: " + authToken);
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
        System.out.println("Creating game: " + gameName);
        CreateGameReq request = new CreateGameReq(authToken, gameName);
        Url clientUrl = new Url("/game", "POST", authToken);
        InputStreamReader reader = clientReader.clientToServer(request, clientUrl);
        if (reader == null) {
            System.out.println("Failed to create game: " + gameName);
        }
        return new Gson().fromJson(reader, CreateGameRes.class);
    }

    public ListGamesRes listGames(String authToken){
//        listGames
        return null;
    };

    public JoinGameRes joinGame(Integer gameID, String playerColor, String authToken){
//        joinGame
        return null;
    };

    public ClearRes clear(){
//        clear
        return null;
    }
}
