package dataaccess;

import model.AuthData;
import java.util.HashMap;


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


}
