package Clienter2Sever;

public class URLStrings {

    String urlPath;
    String requestMethod;
    String authToken;

    public URLStrings(String path, String method, String token) {
        urlPath = path;
        requestMethod = method;
        authToken = token;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public String getRequestBody() {
         return requestMethod;
    }

    public String getAuthToken() {
        return authToken;
    }
}
