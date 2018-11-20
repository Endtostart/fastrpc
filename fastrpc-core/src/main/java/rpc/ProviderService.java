package rpc;

import context.ApplicationContext;
import message.ExceptionResponse;
import message.IRequest;
import message.IResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proxy.ProviderProxyFactory;
import serialize.DefaultJsonSerialize;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class ProviderService {

    Logger logger = LoggerFactory.getLogger(ProviderService.class);

    private StaticPanel staticPanel = (StaticPanel) ApplicationContext.get(StaticPanel.class);

    private DefaultJsonSerialize defaultJsonSerialize = (DefaultJsonSerialize) ApplicationContext.get(DefaultJsonSerialize.class);

    private ProviderProxyFactory providerProxyFactory = (ProviderProxyFactory) ApplicationContext.get(ProviderProxyFactory.class);


    public void setPannel(StaticPanel pannel) {
        this.staticPanel = pannel;
    }

    public String doProcess(String message) {
        if (message == null || message == "") {
            IResponse response = new ExceptionResponse("message is null");
            String resMessage = defaultJsonSerialize.encode(response);
            return resMessage;
        }

        String res = null;
        try {
            res = processor(message);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        if (res == null) {
            IResponse response = new ExceptionResponse(res);
            String resMessage = defaultJsonSerialize.encode(response);
            return resMessage;
        }

        return res;
    }


    public String  processor(String message) throws InvocationTargetException, IllegalAccessException {

        String resMessage = null;

        IRequest request = defaultJsonSerialize.decode(message, IRequest.class);
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
