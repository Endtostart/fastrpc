package proxy;

import context.ApplicationContext;
import message.IResponse;
import message.Response;
import netty.VirtualServer;
import serialize.DefaultJsonSerialize;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ResponseMethodHandler implements InvocationHandler {

    private Object target;
    private DefaultJsonSerialize defaultJsonSerialize = (DefaultJsonSerialize) ApplicationContext.get(DefaultJsonSerialize.class);
    private VirtualServer virtualServer = (VirtualServer) ApplicationContext.get(VirtualServer.class);

    public ResponseMethodHandler(Object target) {
        this.target = target;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object res = method.invoke(target, args);
        IResponse response = new Response();
        response.setValue(res);
        String message = defaultJsonSerialize.encode(response);
        virtualServer.response(message);
        return null;
    }

}
