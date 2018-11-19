import context.ApplicationContext;
import proxy.ClientProxyFactoty;

public class Lab {
    public static void main(String[] args) {
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");


        ApplicationContext.registService(Sample.class, new SampleImpl());
        ClientProxyFactoty proxy = new ClientProxyFactoty();
        Sample sampleProxy = (Sample) proxy.getProxy(Sample.class);

        System.out.println("============= begain ============");
        Person person = sampleProxy.getRequest();
        System.out.println("年龄:" + person.getAge());
        System.out.println("姓名:" + person.getName());
    }
}
