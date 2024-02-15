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
            if (Objects.equals(user.username(), username)) {
                return user;
            }
        }
        return null;
    }


    @Override
    public userData getUser(String username, String password) {
        for (userData user : data) {
            if (Objects.equals(user.username(), username) && Objects.equals(user.password(), password)) {
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
        data.removeIf(user -> Objects.equals(user.username(),username));
    }

    @Override
    public void clear() {
        data.clear();
    }

    static UserDAO getInstance() {
        if (instance == null) {
            instance = new MemoryUserDAO();
        }
        return instance;
    }
}
