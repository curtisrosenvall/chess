package Clienter2Sever;

import com.google.gson.Gson;
import request.RegisterRequest;
import result.RegisterResult;

import java.io.InputStreamReader;

public class ServerFacade {

    ClientCommunicator clientCommunicator;

    public ServerFacade(int port) {
        clientCommunicator = new ClientCommunicator(port);
    }

    public RegisterResult registerUser(String username, String password, String email) {
        RegisterRequest request = new RegisterRequest(username, password, email);
        URLStrings clientStrings = new URLStrings("/user", "POST", "");
        InputStreamReader reader = clientCommunicator.client2Server(request,clientStrings);
        return new Gson().fromJson(reader, RegisterResult.class)
    }
}
