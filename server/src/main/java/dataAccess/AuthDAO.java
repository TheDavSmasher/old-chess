package dataAccess;

import dataModels.authData;

public interface AuthDAO {
    authData getAuth(String token);
    authData createAuth(String username);
    void deleteAuth(String token);
    void clear();
}
