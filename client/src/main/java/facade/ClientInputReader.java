package facade;
import com.google.gson.Gson;
import request.ParentReq;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

public class ClientInputReader {

    private final int port;
    private final Gson gson;

    public ClientInputReader(int port) {
        this.port = port;
        this.gson = new Gson();
    }

    public InputStreamReader clientToServer(ParentReq request, Url clientUrl) {
        InputStreamReader reader = null;
       try {
            String urlString = "http://localhost:" + port + clientUrl.getUrlPath();
            URI uri = new URI(urlString);
            HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setRequestMethod(clientUrl.getRequestMethod());
            connection.setDoOutput(true);
            String authToken = clientUrl.getAuthToken();
            if (authToken != null && !authToken.isEmpty()) {
                connection.addRequestProperty("Authorization", authToken);
            }
            if (!"GET".equalsIgnoreCase(clientUrl.getRequestMethod())) {
                String json = gson.toJson(request);
                try (OutputStream requestBody = connection.getOutputStream()) {
                    requestBody.write(json.getBytes());
                }
            }
            connection.connect();
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new InputStreamReader(connection.getInputStream());
            } else {
                reader = new InputStreamReader(connection.getErrorStream());
            }
        } catch (URISyntaxException e) {
            System.out.println("Error: Invalid URL syntax.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error: IO issue while trying to connect.");
            e.printStackTrace();
        }
        return reader;
    }
}