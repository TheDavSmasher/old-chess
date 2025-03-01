package server;

import service.AppService;
import model.response.result.ServiceException;
import spark.Request;
import spark.Response;
import spark.Spark;

public class ClearHandler extends ObjectSerializer {
    @Override
    public String serviceHandle(Request ignored) throws ServiceException {
        AppService.clearData();
        return "{}";
    }
}
