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
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod(clientStrings.getRequestMethod());
            http.setDoOutput(true);
            http.addRequestProperty("Authorization", clientStrings.getAuthToken());

            if(!clientStrings.getRequestMethod().equals("GET")) {
                try (OutputStream requestBody = http.getOutputStream()) {
                    // Write request body to OutputStream ...
                    String json = new Gson().toJson(request);
                    requestBody.write(json.getBytes());
                }
            }

            // Make the request
            http.connect();
            InputStreamReader reader;
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream responseBody = http.getInputStream();
                // Read response body from InputStream ...
                reader = new InputStreamReader(responseBody);
            }
            else { // SERVER RETURNED AN HTTP ERROR
                InputStream responseBody = http.getErrorStream();
                // Read and process error response body from InputStream ...
                reader = new InputStreamReader(responseBody);
            }
            return reader;

        } catch(URISyntaxException uriException) {
            System.out.println("There is something wrong with my URL");
        } catch(java.net.ProtocolException protocalException) {
            System.out.println("I can't set my method request");
        } catch(IOException ioException) {
            System.out.println("Something went wrong while trying to connect");
        }
        return null;
    }
}