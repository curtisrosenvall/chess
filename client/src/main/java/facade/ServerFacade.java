package facade;
import result.*;

public class ServerFacade {
    ClientInputReader clientReader;

    public ServerFacade(int port){
        clientReader = new ClientInputReader(port);
    }

    public RegisterRes registerUser(String username, String password, String email){
//        register user
    };

    public LoginRes loginUser(String username, String password){
//        login user
    };

    public LogoutRes logoutUser(String authToken){
//        logout User
    };

    public CreateGameRes createGame(String gameName, String AuthToken){
//        createGame
    }

    public ListGamesRes listGames(String authToken){
//        listGames
    };

    public JoinGameRes joinGame(Integer gameID, String playerColor, String authToken){
//        joinGame
    };

    public ClearRes clear(){
//        clear
    }
}
