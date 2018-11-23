package fastrpc.proxy;

import fastrpc.context.annotation.Bean;

import java.lang.reflect.Proxy;

@Bean
public class ClientProxyFactoty<T> implements ProxyFactoty<T>{

    @Override
    public T getProxy(T target) {
        return (T) Proxy.newProxyInstance(ClientProxyFactoty.class.getClassLoader(),new Class[] {target.getClass()},new RequestMethodHandler<T>(target));
    }

    @Override
    public T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(ClientProxyFactoty.class.getClassLoader(),new Class[] {clazz},new RequestMethodHandler<T>(clazz));
    }


}
