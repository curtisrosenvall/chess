package facade;

public class Url {

    String urlPath;
    String requestMethod;
    String authToken;

    public Url(String path, String method, String token){
        urlPath = path;
        requestMethod = method;
        authToken = token;
    }
    public String getUrlPath() {
        return urlPath;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getAuthToken() {
        return authToken;
    }
}
