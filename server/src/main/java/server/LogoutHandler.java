package server;

import service.UserService;
import model.response.result.ServiceException;
import spark.Request;

public class LogoutHandler extends ObjectSerializer {
    @Override
    public String serviceHandle(Request request) throws ServiceException {
        UserService.logout(getAuthToken(request));
        return "{}";
    }
}
