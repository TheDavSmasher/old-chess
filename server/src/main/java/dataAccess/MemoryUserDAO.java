package dataAccess;

import dataModels.userData;

import java.util.HashSet;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {
    static MemoryUserDAO instance;
    private final HashSet<userData> data;

    MemoryUserDAO() {
        data = new HashSet<>();
    }

    @Override
    public userData getUser(String username) {
        for (userData user : data) {
            if (user.username().equals(username)) {
                return user;
            }
        }
        return null;
    }


    @Override
    public userData getUser(String username, String password) {
        for (userData user : data) {
            if (user.username().equals(username) && user.password().equals(password)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void createUser(String username, String password, String email) {
        data.add(new userData(username, password, email));
    }

    @Override
    public void deleteUser(String username) {
        data.removeIf(user -> user.username().equals(username));
    }

    @Override
    public void clear() {
        data.clear();
    }

    public static UserDAO getInstance() {
        if (instance == null) {
            instance = new MemoryUserDAO();
        }
        return instance;
    }
}
