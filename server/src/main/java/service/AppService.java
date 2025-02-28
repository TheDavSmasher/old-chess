package service;

import dataAccess.*;
import model.response.result.ServiceException;

public class AppService extends Service {
    public static void clearData() throws ServiceException {
        tryCatch(() -> {
            AuthDAO.getInstance(true).clear();
            UserDAO.getInstance(true).clear();
            GameDAO.getInstance(true).clear();
        });
    }
}
