package fastrpc.context;

import fastrpc.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractBeanContent implements BeanContent{
    Map<String, Object> context = new HashMap<>();

    @Override
    public <T> T getBeanByName(String name) {
        return (T) context.get(name);
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        if (Objects.isNull(clazz)) {
            return null;
        }

        String name = StringUtils.lowerFirstWorld(clazz.getSimpleName());
        return (T) context.get(name);
    }
}
