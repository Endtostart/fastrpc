package fastrpc.context;

public interface BeanContent {
    <T> T getBeanByName(String name);

    <T> T getBean(Class<T> clazz);
}
