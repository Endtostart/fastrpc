package fastrpc.context;

import fastrpc.netty.base.SocketBuffer;
import fastrpc.proxy.ProviderProxyFactory;
import fastrpc.rpc.ClientService;
import fastrpc.rpc.StaticPanel;
import fastrpc.serialize.SerializeAdapterManager;

import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {

    private static Map<Class, Object> context = new HashMap();

    // TODO 模拟类似于spring的bean容器
    static {
        context.put(SerializeAdapterManager.class, new SerializeAdapterManager());
        context.put(ProviderProxyFactory.class, new ProviderProxyFactory());
        context.put(StaticPanel.class, new StaticPanel());
        context.put(SocketBuffer.class, new SocketBuffer());

        context.put(ClientService.class, new ClientService());
    }

    public static void put(Class clazz, Object object) {
        context.put(clazz, object);
    }

    public static void remove(Class clazz) {
        context.remove(clazz);
    }

    public static boolean exist(Class clazz) {
        return context.containsKey(clazz);
    }

    public static Object get(Class clazz) {
        return context.get(clazz);
    }

    public static void registService(Class clazz, Object object){
        try {
            Class.forName(object.getClass().getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        StaticPanel staticPanel = (StaticPanel) context.get(StaticPanel.class);
        staticPanel.putMapper(clazz, object);
    }

}
