package request;

public class LoginReq extends ParentReq {

    private String username;
    private String password;

    public LoginReq(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
