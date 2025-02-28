package service;

import dataAccess.*;
import model.response.result.ServiceException;

public class AppService extends Service {
    public static void clearData() throws ServiceException {
        tryCatch(() -> {
            AuthDAO.getInstance().clear();
            UserDAO.getInstance().clear();
            GameDAO.getInstance().clear();
        });
    }
}
