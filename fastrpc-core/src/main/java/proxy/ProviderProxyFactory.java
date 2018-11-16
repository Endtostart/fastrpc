package proxy;

public class ProviderProxyFactory<T> implements ProxyFactoty<T>{
    @Override
    public T getProxy(T target) {
        return null;
    }

    @Override
    public T getProxy(Class<T> clazz) {
        return null;
    }
}
