package dataaccess;

import models.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    HashMap<String, UserData> map;

    public MemoryUserDAO() {
        map = new HashMap<>();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public void createUser(String name, UserData authData) {
        map.put(name, authData);
    }

    @Override
    public UserData getUser(String name) {
        return map.get(name);
    }

    @Override
    public int size() {
        return map.size();
    }
}
