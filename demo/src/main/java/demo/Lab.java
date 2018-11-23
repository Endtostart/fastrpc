package demo;

import fastrpc.context.ApplicationConfig;
import fastrpc.context.BootStrap;
import fastrpc.context.RpcBeanContent;
import fastrpc.context.register.ServiceContent;

public class Lab {
    public static void main(String[] args) {
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");


        /*ApplicationContext.registService(demo.Sample.class, new demo.SampleImpl());*/
       /* ApplicationContext.registService(UserService.class, new UserServiceImpl());

        VirtualServer server = (VirtualServer) ApplicationContext.get(VirtualServer.class);
        server.accept();
        ClientProxyFactoty proxy = new ClientProxyFactoty();

        UserService userService = (UserService) proxy.getProxy(UserService.class);
        User user = userService.getUserInfo("123");
        System.out.println(user);*/

        /*
        demo.Sample sampleProxy = (demo.Sample) fastrpc.proxy.getProxy(demo.Sample.class);

        System.out.println("============= begain ============");
        demo.Person person = sampleProxy.getRequest();
        System.out.println("年龄:" + person.getAge());
        System.out.println("姓名:" + person.getName());*/

        ApplicationConfig config = new ApplicationConfig();
        config.setServicePath("demo");

        BootStrap bootStrap = new BootStrap(config);
        bootStrap.start();

    }
}
