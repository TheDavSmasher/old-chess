package server.handler;

import model.request.UserEnterRequest;
import model.response.UserEnterResponse;
import model.response.result.ServiceException;
import server.ObjectSerializer;
import spark.Request;

public class UserEnterHandler extends ObjectSerializer {
    private final UserEnter userEnter;

    public UserEnterHandler(UserEnter userEnter) {
        this.userEnter = userEnter;
    }

    @Override
    protected String serviceHandle(Request request) throws ServiceException {
        UserEnterRequest userEnterRequest = deserialize(request, UserEnterRequest.class);
        UserEnterResponse userEnterResponse = userEnter.handleEnter(userEnterRequest);
        return serialize(userEnterResponse);
    }
}
