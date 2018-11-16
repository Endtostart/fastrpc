package message;

import java.io.Serializable;
import java.lang.reflect.Type;

public class Request implements Serializable,IRequest{

    private static final long serialVersionUID = 1168814620391610215L;

    private String requestId;

    private String interfaceName;

    private String methodName;

    private Type[] paramTypes;

    private Object[] params;

    public Request() {

    }

    @Override
    public String getRequestId() {
        return requestId;
    }

    @Override
    public String getInterfaceName() {
        return interfaceName;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public Type[] getParamTypes() {
        return paramTypes;
    }

    @Override
    public Object[] getParams() {
        return params;
    }

    @Override
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    @Override
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public void setParamTypes(Type[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    @Override
    public void setParams(Object[] params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "\n requestId :" + requestId + "\n interfaceName: " + interfaceName + "\n methodName: " + methodName;
    }
}
