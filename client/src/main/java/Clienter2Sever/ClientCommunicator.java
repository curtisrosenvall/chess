package Clienter2Sever;

import request.ParentRequest;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;

public class ClientCommunicator {

    int port;

    public ClientCommunicator(int port) {
        this.port = port;
    }

    public InputStream client2Server(ParentRequest request, URLStrings clientStrings){
        try{
            String ulrString = "http://localhost:" + port + clientStrings.getUrlPath();
            URI uri = new URI(ulrString);
            HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setRequestMethod(clientStrings.getRequestMethod());
            connection.setDoOutput(true);
            connection.addRequestProperty("Authorization", clientStrings.getAuthToken());

            if(!clientStrings.getRequestMethod().equals("GET")){
                try (OutputStream requestBody = connection.getOutputStream()) {
                    
                }
            }
        }
    }
}
