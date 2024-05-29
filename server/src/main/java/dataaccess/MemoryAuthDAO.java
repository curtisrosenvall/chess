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

    @Override
    public void clear(){
        mapOfAuthData.clear();
    }

    @Override
    public void createAuth(String token, AuthData data){
        mapOfAuthData.put(token, data);
    }

    @Override
    public void deleteAuth(String token){
        mapOfAuthData.remove(token);
    }

    @Override
    public AuthData getAuth(String token){
        return mapOfAuthData.get(token);
    }

    @Override
    public int size(){
        return mapOfAuthData.size();
    }

    public ArrayList<AuthData> getAuth() {
        ArrayList<AuthData> allAuths = new ArrayList<>();
        for(Map.Entry<String, AuthData> entry : mapOfAuthData.entrySet()) {
            allAuths.add(entry.getValue());
        }
        return allAuths;
    }

}
