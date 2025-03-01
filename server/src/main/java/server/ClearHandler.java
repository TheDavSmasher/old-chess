package server;

import model.response.EmptyResponse;
import service.AppService;
import model.response.result.ServiceException;
import spark.Request;

public class ClearHandler extends ObjectSerializer<EmptyResponse> {
    @Override
    public EmptyResponse serviceHandle(Request ignored) throws ServiceException {
        return AppService.clearData();
    }
}
