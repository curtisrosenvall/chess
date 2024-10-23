package request;

public class LogoutReq extends ParentReq{

    private String authToken;

    public LogoutReq(String authToken) {
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