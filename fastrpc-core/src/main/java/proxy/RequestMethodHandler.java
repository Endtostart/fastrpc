package proxy;

import exception.RpcException;
import message.DefaultMessageHandler;
import message.IRequest;
import message.IResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.ClientService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Objects;

public class RequestMethodHandler<T> implements InvocationHandler {

    Logger logger = LoggerFactory.getLogger(RequestMethodHandler.class);

    T target;
    Class<T> clazz;
    ClientService clientService;

    public RequestMethodHandler(Class<T> clazz) {
        this.clazz = clazz;
        if (Objects.isNull(clientService)) {
            clientService = new ClientService();
        }
    }

    public RequestMethodHandler(T target) {
        this.target = target;
        if (Objects.isNull(clientService)) {
            clientService = new ClientService();
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Method[] methods = clazz.getDeclaredMethods();
        for (Method m : methods) {
            if (m.getName().equals(method.getName())) {
                IRequest request = DefaultMessageHandler.buildRequest(clazz, method, args);
                try {
                    Type returnType = method.getReturnType();
                    IResponse response = clientService.doCall(request,returnType);
                    Object value = response.getValue();
                    logger.info("返回结果:" + value.toString());
                    return value;
                } catch (Exception e) {
                    logger.info("调用失败...");
                    throw new RpcException("调用失败");
                }
            }
        }
        return null;
    }
}
