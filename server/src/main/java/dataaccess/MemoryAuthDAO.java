package dataaccess;

import java.util.ArrayList;
import model.AuthData;
import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {

    HashMap<String, AuthData> mapOfAuthData;

    public MemoryAuthDAO() {
        mapOfAuthData = new HashMap<>();
    }

    public void clear(){
        mapOfAuthData.clear();
    }

    public void createAuth(String token, AuthData data){
        mapOfAuthData.put(token, data);
    }

    public void deleteAuth(String token){
        mapOfAuthData.remove(token);
    }

    public AuthData getAuth(String token){
        return mapOfAuthData.get(token);
    }

    public int size(){
        return mapOfAuthData.size();
    }

    public ArrayList<AuthData> getAllAuths() {
        ArrayList<AuthData> allAuths = new ArrayList<>();
        for(Map.Entry<String, AuthData> entry : mapOfAuthData.entrySet()) {
            allAuths.add(entry.getValue());
        }
        return allAuths;
    }

}
