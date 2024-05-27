package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    HashMap<String, UserData> mapOfUserData;

    public MemoryUserDAO() {
        mapOfUserData = new HashMap<>();
    }

    public void clear(){
        mapOfUserData.clear();
    }

    public void createUser(String username, UserData authData){
        mapOfUserData.put(username, authData);
    }

    public void deleteUser (String name){
        mapOfUserData.remove(name);
    }

    public UserData getUser(String name){
        return mapOfUserData.get(name);
    }

    public int size(){
        return mapOfUserData.size();
    }

}
