package fastrpc.context;

import fastrpc.context.config.ApplicationConfig;
import fastrpc.context.register.ServiceContent;
import fastrpc.context.support.ApplicationUtils;
import fastrpc.rpc.ClientService;
import fastrpc.transport.jdknio.NioServer;
import fastrpc.utils.ClassUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class BootStrap {
    private ApplicationConfig config;
    private RpcBeanContent beanContent;
    private ServiceContent serviceContent;

    public BootStrap(ApplicationConfig config) {
        this.config = config;
    }
    public void server() {
        start();
        String servicePath = config.getServicePath();
        if (servicePath == null) {
            return;
        }
        serviceContent = new ServiceContent();
        serviceContent.register(servicePath);

        NioServer nioServer = new NioServer(ApplicationUtils.getServerPort());
        nioServer.start();
    }

    public void customer() {
        start();
        ClientService clientService = beanContent.getBean(ClientService.class);
        clientService.startClient();
    }

    private void start() {
        beanContent = new RpcBeanContent();
        beanContent.init();

        // 后置依赖处理
        postDepandence();

        // 植入全局操作
        ApplicationUtils.setBeanContent(beanContent);
    }

    private void start(ApplicationConfig config) {
        this.config = config;
        ApplicationUtils.setApplicationConfig(config);
        start();
    }

    private void postDepandence() {
        /**
         * TODO
         * 这里写的过于固定化，目的。根据接口继承，注入服务方法上下文
         */
        Map<String, Object> objectMap = beanContent.getBeanNameInstanceMapper();
        for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
            Object object = entry.getValue();
            if (object instanceof ServiceApplicationAwake) {
                Method method = ServiceApplicationAwake.class.getDeclaredMethods()[0];

                Method[] methods = object.getClass().getDeclaredMethods();
                for (Method method1 : methods) {
                    if (ClassUtils.compareMethod(method1, method)) {
                        try {
                            method1.invoke(object, this.serviceContent);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public Strap strap() {
        return new BestStrap();
    }

    class BestStrap implements Strap{

        public BestStrap() {

        }

        public BestStrap(ApplicationConfig conf) {
            config = conf;
        }

        @Override
        public void service() {
            server();
        }

        @Override
        public void start() {
            customer();
        }
    }


    public ServiceContent getServiceContent() {
        return serviceContent;
    }

    public RpcBeanContent getBeanContent() {
        return beanContent;
    }
}
