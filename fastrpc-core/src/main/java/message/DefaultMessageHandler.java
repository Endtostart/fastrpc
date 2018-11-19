package message;

import utils.UuidHelper;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class DefaultMessageHandler {

    public static IRequest buildRequest(Class clazz, Method method, Object[] args) {
        String requestId = UuidHelper.uuid();
        Type[] paramTypes = method.getParameterTypes();
        String interfaceName = clazz.getName();
        String methodName = method.getName();

        IRequest request = new Request();
        request.setRequestId(requestId);
        request.setParamTypes(paramTypes);
        request.setInterfaceName(interfaceName);
        request.setMethodName(methodName);
        request.setParams(args);
        return request;
    }

}
