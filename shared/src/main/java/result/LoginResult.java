package result;

public class LoginResult extends ParentResult{
    private final String username;
    private final String authToken;

    public LoginResult(Boolean success, String message, String username, String authToken) {
        super(success,message);
        this.username = username;
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }
}
