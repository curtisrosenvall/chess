package result;

public class RegisterRes extends ParentRes{

    private final String username;
    private final String authToken;

    public RegisterRes(Boolean success, String message, String username, String authToken) {
        super(success, message);
        this.username = username;
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }

    @Override
    public String toString() {
        return ("Register Result:\n" +
                "Success: " + isSuccess() + "\n" +
                "Username: " + username + "\n" +
                "AuthToken: " + authToken + "\n");

    }
}
