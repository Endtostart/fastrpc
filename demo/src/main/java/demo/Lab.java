package demo;

import fastrpc.context.config.ApplicationConfig;
import fastrpc.context.BootStrap;
import fastrpc.context.RpcBeanContent;
import fastrpc.context.config.ServiceConfig;
import fastrpc.netty.VirtualServer;
import fastrpc.proxy.ClientProxyFactoty;

public class Lab {
    public static void main(String[] args) {
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

        ApplicationConfig config = new ApplicationConfig();
        ServiceConfig serviceConfig = new ServiceConfig();
        serviceConfig.setScanPath("demo");
        config.setServiceConfig(serviceConfig);
        BootStrap bootStrap = new BootStrap(config);
        bootStrap.start();

        RpcBeanContent content = bootStrap.getBeanContent();
        VirtualServer server = content.getBean(VirtualServer.class);
        server.accept();

        ClientProxyFactoty client = content.getBean(ClientProxyFactoty.class);
        UserService userService = (UserService) client.getProxy(UserService.class);
        User user = userService.getUserInfo("123");

        System.out.println(user);
    }
}
