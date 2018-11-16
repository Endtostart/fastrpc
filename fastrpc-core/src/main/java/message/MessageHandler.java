package message;

import java.lang.reflect.Method;

public interface MessageHandler {
    IRequest buildRequest(Class clazz, Method method, Object[] args);
}
