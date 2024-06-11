package Clienter2Sever;

import com.google.gson.Gson;
import request.ParentRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public class ClientCommunicator {

    int port;

    public ClientCommunicator(int port) {
        this.port = port;
    }

    public InputStreamReader client2Server(ParentRequest request, URLStrings clientStrings){
        try{
            String ulrString = "http://localhost:" + port + clientStrings.getUrlPath();
            URI uri = new URI(ulrString);
            HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setRequestMethod(clientStrings.getRequestMethod());
            connection.setDoOutput(true);
            connection.addRequestProperty("Authorization", clientStrings.getAuthToken());

            if(!clientStrings.getRequestMethod().equals("GET")){
                try (OutputStream requestBody = connection.getOutputStream()) {
                   String json = new Gson().toJson(request);
                   requestBody.write(json.getBytes(StandardCharsets.UTF_8));
                }
            }
            connection.connect();
            InputStreamReader reader;
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream responseBOdy = connection.getInputStream();
                reader = new InputStreamReader(responseBOdy);
            }
            else {
                InputStream responseBody = connection.getErrorStream();
                reader = new InputStreamReader(responseBody);
            }
            return reader;

        } catch (URISyntaxException URIe) {
            System.out.println("Something messed up with URL");
        } catch (java.net.ProtocolException ProtocolEx) {
            System.out.println("Something messed up with protocol");
        } catch (IOException IOEx) {
            System.out.println("Something messed up with server");
        }
        return null;
    }
}
