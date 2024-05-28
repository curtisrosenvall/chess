package request;

public class LogoutRequest extends ParentRequest{

    private String authToken;

    public LogoutRequest(String authToken) {
        super();
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
