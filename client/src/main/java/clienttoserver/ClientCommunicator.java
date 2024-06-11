package clienttoserver;
import com.google.gson.Gson;
import request.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

public class ClientCommunicator {

    int port;

    public ClientCommunicator(int port) {
        this.port = port;
    }

    public InputStreamReader clientToServer(ParentRequest request, URLStrings clientStrings) {
        try {
            String urlString = "http://localhost:" + port + clientStrings.getUrlPath();
            URI uri = new URI(urlString);
            HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setRequestMethod(clientStrings.getRequestMethod());
            connection.setDoOutput(true);
            connection.addRequestProperty("Authorization", clientStrings.getAuthToken());

            if(!clientStrings.getRequestMethod().equals("GET")) {
                try (OutputStream requestBody = connection.getOutputStream()) {
                    String json = new Gson().toJson(request);
                    requestBody.write(json.getBytes());
                }
            }

            connection.connect();
            InputStreamReader reader;
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream responseBody = connection.getInputStream();
                reader = new InputStreamReader(responseBody);
            }
            else {
                InputStream responseBody = connection.getErrorStream();
                reader = new InputStreamReader(responseBody);
            }
            return reader;

        } catch(URISyntaxException uriException) {
            System.out.println("Something is wrong with the URL donnie");
        } catch(java.net.ProtocolException protocolException) {
            System.out.println("I can't set my method request");
        } catch(IOException ioException) {
            System.out.println("Cannot connect to server");
        }
        return null;
    }
}