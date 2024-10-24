package request;


public class JoinGameReq extends ParentReq {

    private String authToken;
    private final String playerColor;
    private final int gameID;

    public JoinGameReq(String authToken, String playerColor, int gameID) {
        super();
        this.authToken = authToken;
        this.playerColor = playerColor;
        this.gameID = gameID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public int getGameID() {
        return gameID;
    }

    public void setAuthToken(String token) {
        authToken = token;
    }
}