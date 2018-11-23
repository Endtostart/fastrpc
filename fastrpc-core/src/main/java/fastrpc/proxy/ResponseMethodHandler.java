package fastrpc.proxy;

import fastrpc.context.annotation.Bean;
import fastrpc.context.annotation.Weave;
import fastrpc.message.IResponse;
import fastrpc.message.Response;
import fastrpc.netty.VirtualServer;
import fastrpc.serialize.GeneralSerialize;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Bean(instance = false)
public class ResponseMethodHandler implements InvocationHandler {

    private Object target;
    @Weave
    private static GeneralSerialize generalSerialize;
    @Weave
    private static VirtualServer virtualServer;

    public ResponseMethodHandler(Object target) {
        this.target = target;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object res = method.invoke(target, args);
        IResponse response = new Response();
        response.setValue(res);
        byte[] message = generalSerialize.encodeToByte(response);
        virtualServer.response(message);
        return null;
    }

}
