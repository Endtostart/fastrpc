package fastrpc.proxy;

import fastrpc.context.annotation.Bean;
import fastrpc.context.annotation.Weave;
import fastrpc.exception.RpcException;
import fastrpc.message.DefaultMessageHandler;
import fastrpc.message.ExceptionResponse;
import fastrpc.message.IRequest;
import fastrpc.message.IResponse;
import fastrpc.rpc.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Objects;

@Bean(instance = false)
public class RequestMethodHandler<T> implements InvocationHandler {

    Logger logger = LoggerFactory.getLogger(RequestMethodHandler.class);

    T target;
    Class<T> clazz;
    @Weave
    private static ClientService clientService;

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
                    if (response.hasException()) {
                        RpcException rpcException = ((ExceptionResponse) response).getValue();
                        throw rpcException;
                    }
                    Object value = response.getValue();
                    logger.info("返回结果:" + value.toString());
                    return value;
                }catch (RpcException ex){
                    logger.info("调用处理失败 :" + ex.getMessage());
                    return null;
                }catch (Exception e) {
                    logger.info("调用失败...");
                    throw new RpcException("调用失败");
                }
            }
        }
        return null;
    }
}
