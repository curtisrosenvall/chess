package facade;
import com.google.gson.Gson;
import request.RegisterReq;
import result.*;

import java.io.InputStreamReader;

public class ServerFacade {
    ClientInputReader clientReader;

    public ServerFacade(int port){
        clientReader = new ClientInputReader(port);
    }

    public RegisterRes registerUser(String username, String password, String email){
        System.out.println("Registering user: "+ username);
        RegisterReq request = new RegisterReq(username,password,email);
        Url clientUrl = new Url("/user", "POST", "");
        InputStreamReader reader = clientReader.clientToServer(request,clientUrl);
        if(reader == null){
            System.out.println("[FAILED] User was not registered: " + username);
        }
        return new Gson().fromJson(reader,RegisterRes.class);
    };

    public LoginRes loginUser(String username, String password){
//        login user
        return null;
    };

    public LogoutRes logoutUser(String authToken){
//        logout User
        return null;
    };

    public CreateGameRes createGame(String gameName, String AuthToken){
//        createGame
        return null;
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
