package server.handler;

import model.request.UserEnterRequest;
import model.response.UserEnterResponse;
import model.response.result.ServiceException;

public interface UserEnter {
    UserEnterResponse handleEnter(UserEnterRequest request) throws ServiceException;
}
