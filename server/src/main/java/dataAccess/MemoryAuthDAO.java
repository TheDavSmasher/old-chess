package dataAccess;

import dataModels.authData;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;


public class MemoryAuthDAO implements AuthDAO {
    static MemoryAuthDAO instance;
    private final HashSet<authData> data;

    MemoryAuthDAO() {
        data = new HashSet<>();

    }
    @Override
    public authData getAuth(String token) {
        for (authData auth : data) {
            if (auth.authToken().equals(token)) {
                return auth;
            }
        }
        return null;
    }

    @Override
    public authData createAuth(String username) {
        String token = UUID.randomUUID().toString();
        authData newAuth = new authData(username, token);
        data.add(newAuth);
        return newAuth;
    }

    @Override
    public void deleteAuth(String token) {
        data.removeIf(user -> Objects.equals(user.authToken(), token));
    }

    @Override
    public void clear() {
        data.clear();
    }

    static public AuthDAO getInstance() {
        if (instance == null) {
            instance = new MemoryAuthDAO();
        }
        return instance;
    }
}
