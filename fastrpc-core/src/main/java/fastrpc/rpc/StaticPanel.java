package fastrpc.rpc;

import fastrpc.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@Bean
public class StaticPanel {
    private Map<Class, Object> serviceMapper = new HashMap<>();


    public Map<Class, Object> getMappers() {
        return serviceMapper;
    }

    public <T> T getInstance(Class<T> clazz) {
        return (T) serviceMapper.get(clazz);
    }

    public Object remove(Class clazz) {
        return serviceMapper.remove(clazz);
    }

    public Map<Class, Object> putMapper(Class clazz, Object object) {
        serviceMapper.put(clazz, object);
        return serviceMapper;
    }

}
