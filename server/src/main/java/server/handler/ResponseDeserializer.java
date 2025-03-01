package server.handler;

import model.response.result.ServiceException;
import server.ObjectSerializer;
import spark.Request;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class ResponseDeserializer<T, U>  extends ObjectSerializer<U> {
    protected T deserialize(Request request) {
        Type generic = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return gson.fromJson(request.body(), generic);
    }

    protected abstract U serviceDeserialize(T serviceRequest, String authToken) throws ServiceException;

    @Override
    protected U serviceHandle(Request request) throws ServiceException {
        T serviceRequest = deserialize(request);
        return serviceDeserialize(serviceRequest, getAuthToken(request));
    }
}
