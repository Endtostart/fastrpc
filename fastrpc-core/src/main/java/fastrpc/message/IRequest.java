package fastrpc.message;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public interface IRequest {

    public String getRequestId();

    public String getInterfaceName();

    public String getMethodName();

    public Type[] getParamTypes();

    public Object[] getParams();

    public void setRequestId(String requestId);

    public void setInterfaceName(String interfaceName);

    public void setMethodName(String methodName);

    public void setParamTypes(Type[] paramTypes);

    public void setParams(Object[] params);

}
