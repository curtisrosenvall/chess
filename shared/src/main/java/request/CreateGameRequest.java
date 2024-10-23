package request;

public class CreateGameRequest  extends ParentReq{

    private String authToken;
    private String gameName;

    public CreateGameRequest(String authToken, String gameName) {
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