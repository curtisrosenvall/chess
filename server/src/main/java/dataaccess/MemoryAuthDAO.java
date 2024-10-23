package dataaccess;

import models.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO{
    HashMap<String, AuthData> map;

    public MemoryAuthDAO() {
        map = new HashMap<>();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public void createAuth(String token, AuthData data) {
        map.put(token, data);
    }

    @Override
    public void deleteAuth(String token) {
        map.remove(token);
    }

    @Override
    public AuthData getAuth(String token) {
        return map.get(token);
    }

    @Override
    public int size() {
        return map.size();
    }
}
