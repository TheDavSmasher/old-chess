package server.handler;

import model.response.result.ServiceException;
import spark.Request;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class RequestDeserializer<T, U>  extends ObjectSerializer<U> {
    @Override
    protected U serviceHandle(Request request) throws ServiceException {
        Type requestType = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        T serviceRequest = gson.fromJson(request.body(), requestType);
        return serviceCall(serviceRequest, getAuthToken(request));
    }

    protected abstract U serviceCall(T serviceRequest, String authToken) throws ServiceException;
}
