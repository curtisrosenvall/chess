package request;

public class JoinGameRequest extends ParentRequest{

    private String authToken;
    private final String playerColor;
    private final int gameId;

    public JoinGameRequest(String authToken, String playerColor, int gameId) {
        super();
        this.authToken = authToken;
        this.playerColor = playerColor;
        this.gameId = gameId;
    }

    public String getAuthToken() {
        return authToken;
    }
    public void setAuthToken(String token) {
        authToken = authToken;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public int getGameId() {
        return gameId;
    }
}
