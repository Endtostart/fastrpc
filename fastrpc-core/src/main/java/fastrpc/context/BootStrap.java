package fastrpc.context;

import fastrpc.context.register.ServiceContent;

public class BootStrap {
    private ApplicationConfig config;
    private RpcBeanContent beanContent;
    private ServiceContent serviceContent;

    public BootStrap(ApplicationConfig config) {
        this.config = config;
    }

    public void start() {
        beanContent = new RpcBeanContent();
        beanContent.init();

        String servicePath = config.getServicePath();
        if (servicePath == null) {
            return;
        }
        serviceContent = new ServiceContent();
        serviceContent.register(servicePath);
    }

    public void start(ApplicationConfig config) {
        this.config = config;
        start();
    }

    public ServiceContent getServiceContent() {
        return serviceContent;
    }

    public RpcBeanContent getBeanContent() {
        return beanContent;
    }
}
