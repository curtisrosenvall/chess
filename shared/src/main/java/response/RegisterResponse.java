package response;

public class RegisterResponse extends ParentResponse {

    private String username;
    private String authToken;

    public RegisterResponse(Boolean success, String message,String username, String authToken) {
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
        return "RegisterResponse{" +
                "Success" + isSuccess() + "\n" +
                "username='" + username + '\n' +
                ", authToken='" + authToken + '\n' +
                '}';
    }
}
