package server.handler;

import model.request.UserEnterRequest;
import model.response.UserEnterResponse;
import model.response.result.ServiceException;

public class UserEnterHandler extends RequestDeserializer<UserEnterRequest, UserEnterResponse> {
    private final UserEnter userEnter;

    public UserEnterHandler(UserEnter userEnter) {
        this.userEnter = userEnter;
    }

    @Override
    protected UserEnterResponse serviceCall(UserEnterRequest userEnterRequest, String ignored) throws ServiceException {
        return userEnter.handleEnter(userEnterRequest);
    }
}
