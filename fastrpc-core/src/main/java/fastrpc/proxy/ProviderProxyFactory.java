package fastrpc.proxy;

import fastrpc.context.annotation.Bean;

import java.lang.reflect.Proxy;

@Bean
public class ProviderProxyFactory<T> implements ProxyFactoty<T>{
    @Override
    public T getProxy(T target) {
        return (T) Proxy.newProxyInstance(ProviderProxyFactory.class.getClassLoader(),target.getClass().getInterfaces(),new ResponseMethodHandler(target));
    }

    @Override
    public T getProxy(Class<T> clazz) {
        return null;
    }
}
