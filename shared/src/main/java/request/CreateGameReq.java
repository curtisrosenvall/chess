package request;

public class CreateGameReq  extends ParentReq{

    private String authToken;
    private final String gameName;

    public CreateGameReq(String authToken, String gameName) {
        super();
        this.authToken = authToken;
        this.gameName = gameName;

    }

    public String getAuthToken() {
        return authToken;
    }

    public String getGameName() {
        return gameName;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}