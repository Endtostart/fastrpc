package fastrpc.rpc;

import fastrpc.context.annotation.Bean;
import fastrpc.context.annotation.Weave;
import fastrpc.exception.RpcCallbackException;
import fastrpc.message.ExceptionResponse;
import fastrpc.message.IRequest;
import fastrpc.message.IResponse;
import fastrpc.message.Request;
import fastrpc.proxy.ProviderProxyFactory;
import fastrpc.serialize.GeneralSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

@Bean
public class ProviderService {

    Logger logger = LoggerFactory.getLogger(ProviderService.class);

    @Weave
    private StaticPanel staticPanel;
    @Weave
    private GeneralSerialize generalSerialize;
    @Weave
    private ProviderProxyFactory providerProxyFactory;


    public void setPannel(StaticPanel pannel) {
        this.staticPanel = pannel;
    }

    public void doProcess(byte[] message) {
        if (message == null || message.length == 0) {
            IResponse response = new ExceptionResponse("fastrpc.message is null");
            byte[] resMessage = generalSerialize.encodeToByte(response);
            throw new RpcCallbackException("调用数据包非法", message);
        }

        String res = null;
        try {
            IRequest request = generalSerialize.decode(message, Request.class);
            res = processor(request);
            return;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        if (res == null) {
            IResponse response = new ExceptionResponse(res);
            byte[] resMessage = generalSerialize.encodeToByte(response);
            throw new RpcCallbackException("处理结果异常", message);
        }
    }


    public String  processor(IRequest request) throws InvocationTargetException, IllegalAccessException {

        String resMessage = "";

        String interfaceName = request.getInterfaceName();
        Class clazz = null;
        try {
            clazz = Class.forName(interfaceName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Object target = null;

        Map<Class, Object> mappers = staticPanel.getMappers();
        if (mappers == null || mappers.size() == 0) {
            logger.info("Can't match this service. back...");
            return null;
        }

        for (Map.Entry<Class, Object> entry : mappers.entrySet()) {
            if (clazz.isAssignableFrom(entry.getKey())) {
                target = entry.getValue();
            }
        }

        if (target == null) {
            logger.info("Can't match this service. back...");
            return null;
        }

        target = providerProxyFactory.getProxy(target);

        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            // TODO 未处理重载的情况
            if (method.getName().equals(request.getMethodName())) {
                method.invoke(target, request.getParams());
            }
        }

        return resMessage;
    }
}
