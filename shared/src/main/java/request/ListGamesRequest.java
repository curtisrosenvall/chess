package request;

public class ListGamesRequest extends ParentRequest {

    private final String authToken;

    public ListGamesRequest(String authToken) {
        super();
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }
}
