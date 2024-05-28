package response;

public class LoginResponse extends ParentResponse{
    private String username;
    private String authToken;

    public LoginResponse(Boolean success, String message, String username, String authToken) {
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
