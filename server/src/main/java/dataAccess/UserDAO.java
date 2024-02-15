package dataAccess;

import dataModels.userData;

public interface UserDAO {
    userData getUser(String username);
    userData getUser(String username, String password);
    void createUser(String username, String password, String email);
    void deleteUser(String username);
    void clear();
}
