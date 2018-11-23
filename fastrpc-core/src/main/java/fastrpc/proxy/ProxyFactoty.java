package fastrpc.proxy;

public interface ProxyFactoty<T> {
    T getProxy(T target);

    T getProxy(Class<T> clazz);
}
