package server;

import service.UserService;
import model.request.AuthRequest;
import model.response.result.ServiceException;
import model.response.result.UnauthorizedException;
import spark.Request;
import spark.Response;
import spark.Spark;

public class LogoutHandler extends ObjectSerializer {
    @Override
    public String serviceHandle(Request request) throws ServiceException {
        AuthRequest logoutRequest = new AuthRequest(getAuthToken(request));
        UserService.logout(logoutRequest);
        return "{}";
    }
}
