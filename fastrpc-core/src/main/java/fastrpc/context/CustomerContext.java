package fastrpc.context;

import fastrpc.context.annotation.Bean;
import fastrpc.context.annotation.RpcConsume;
import fastrpc.context.annotation.Weave;
import fastrpc.context.support.BeanUtils;
import fastrpc.proxy.ClientProxyFactoty;
import fastrpc.utils.StringUtils;

import java.util.List;

@Bean
public class CustomerContext extends AbstractBeanContent {

    @Weave
    ClientProxyFactoty clientProxyFactoty;

    public <T> T register(String name, T object) {

        return (T) context.put(name,object);
    }

    public void register(String packagePath) {
        List<Class> classes = BeanUtils.getClass(packagePath, RpcConsume.class);
        if (classes == null || classes.size() == 0) {
            return;
        }

        for (Class clazz : classes) {
            RpcConsume rpcConsume = (RpcConsume) clazz.getAnnotation(RpcConsume.class);
            String name = rpcConsume.name();
            if (name.equals("")) {
                name = StringUtils.lowerFirstWorld(clazz.getSimpleName());
            }
            Object object = clientProxyFactoty.getProxy(clazz);
            context.put(name, object);
        }
    }
}
