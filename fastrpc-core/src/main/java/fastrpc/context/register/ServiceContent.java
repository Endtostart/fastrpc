package fastrpc.context.register;

import fastrpc.context.annotation.Bean;
import fastrpc.context.annotation.RpcService;
import fastrpc.context.support.BeanUtils;
import fastrpc.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Bean
public class ServiceContent implements Register{
    private Map<String, Object> serviceMapper = new HashMap<>();

    @Override
    public <T> T register(String name, Class<T> target) {
        T object = BeanUtils.getBeanInstance(target);
        serviceMapper.put(name, object);
        return object;
    }

    @Override
    public void register(String packagePath) {
        List<Class> classList = BeanUtils.getClass(packagePath, RpcService.class);
        if (Objects.isNull(classList) || classList.isEmpty()) {
            return;
        }

        for (Class clazz : classList) {
            RpcService annotation = (RpcService) clazz.getAnnotation(RpcService.class);
            String name = annotation.name();
            if (name.equals("")) {
                name = StringUtils.lowerFirstWorld(clazz.getInterfaces()[0].getSimpleName());
            }
            Object object = BeanUtils.getBeanInstance(clazz);
            serviceMapper.put(name, object);
        }

    }

    public Map<String, Object> getServiceMapper() {
        return serviceMapper;
    }
}
