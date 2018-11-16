import message.IRequest;
import message.IResponse;
import proxy.ClientProxyFactoty;

public class Lab {
    public static void main(String[] args) {
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        ClientProxyFactoty proxy = new ClientProxyFactoty();
        Sample sampleProxy = (Sample) proxy.getProxy(Sample.class);

        System.out.println("============= begain ============");
        IRequest request = sampleProxy.getRequest();

        System.out.println("MethodName : " + request.getMethodName());
        System.out.println("interfaceName : " + request.getInterfaceName());

    }
}
