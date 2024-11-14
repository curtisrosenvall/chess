package facade;
import com.google.gson.Gson;
import request.ParentReq;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

public class ClientInputReader {

    int port;

    public ClientInputReader(int port) {
        this.port = port;
    }

    public InputStreamReader clientToServer(ParentReq request, Url clientUrl) {
        InputStreamReader reader = null;

        try {
            // Construct the URL for the HTTP connection
            String urlString = "http://localhost:" + port + clientUrl.getUrlPath();
            URI uri = new URI(urlString);

            // Open HTTP connection
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod(clientUrl.getRequestMethod());
            http.setDoOutput(true);
            http.addRequestProperty("Authorization", clientUrl.getAuthToken());

            // Write request body if the request method is not GET
            if (!clientUrl.getRequestMethod().equals("GET")) {
                try (OutputStream requestBody = http.getOutputStream()) {
                    String json = new Gson().toJson(request);
                    requestBody.write(json.getBytes());
                }
            }

            // Connect to the server
            http.connect();

            // Log the HTTP response code and headers for debugging
            int responseCode = http.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            System.out.println("Response Message: " + http.getResponseMessage());

            // Get response and create InputStreamReader based on response code
            InputStream responseBody;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                responseBody = http.getInputStream();
            } else {
                responseBody = http.getErrorStream();
                System.out.println("Error Stream: " + responseBody);
            }

            if (responseBody != null) {
                reader = new InputStreamReader(responseBody);
            } else {
                System.out.println("Error: Response body is null.");
            }

        } catch (URISyntaxException uriException) {
            System.out.println("Error: Invalid URL syntax.");
            uriException.printStackTrace();
        } catch (java.net.ProtocolException protocolException) {
            System.out.println("Error: Protocol setting issue.");
            protocolException.printStackTrace();
        } catch (IOException ioException) {
            System.out.println("Error: IO issue while trying to connect.");
            ioException.printStackTrace();
        }

        return reader;
    }
}