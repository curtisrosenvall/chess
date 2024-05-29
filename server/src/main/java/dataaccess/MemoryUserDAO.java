package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    HashMap<String, UserData> mapOfUserData;

    public MemoryUserDAO() {
        mapOfUserData = new HashMap<>();
    }


    @Override
    public void clear(){
        mapOfUserData.clear();
    }

    @Override
    public void createUser(String username, UserData authData){
        mapOfUserData.put(username, authData);
    }

    @Override
    public void deleteUser (String name){
        mapOfUserData.remove(name);
    }

    @Override
    public UserData getUser(String name){
        return mapOfUserData.get(name);
    }

    @Override
    public int size(){
        return mapOfUserData.size();
    }

}
