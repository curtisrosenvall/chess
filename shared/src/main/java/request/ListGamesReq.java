package request;


public class ListGamesReq extends ParentReq {

    private final String authToken;

    public ListGamesReq(String authToken) {
        super();
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }
}