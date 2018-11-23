package fastrpc.serialize;

import com.alibaba.fastjson.JSON;
import fastrpc.context.annotation.Bean;
import fastrpc.exception.UnSupportException;
import fastrpc.message.IRequest;
import fastrpc.message.IResponse;
import fastrpc.message.Request;
import fastrpc.message.Response;

import java.lang.reflect.Type;

@Bean
public class DefaultJsonSerialize implements GeneralSerialize {

    @Override
    public <T> T decode(String json, Type type) {
        return (T)JSON.parseObject(json,type);
    }

    @Override
    public <T> T decode(byte[] bytes, Class clazz) {
        throw new UnSupportException("Json fastrpc.serialize unsupport params type: byte[]");
    }

    @Override
    public String encode(Object orign) {
        return JSON.toJSONString(orign);
    }

    @Override
    public byte[] encodeToByte(Object orign) {
        String res = encode(orign);
        return res.getBytes();
    }


    public static void main(String[] args) {
        IRequest request = new Request();
        request.setMethodName("hello");
        request.setInterfaceName("PublicClass");

        DefaultJsonSerialize helper = new DefaultJsonSerialize();
        String text = helper.encode(request);
        System.out.println("text:" + text);

        IRequest request1 = (IRequest) helper.decode(text, IRequest.class);
        System.out.println("methodName : " + request1.getMethodName());
        System.out.println("interfaceName : " + request1.getInterfaceName());

        String s = "hello";
        IResponse<IRequest> response = new Response();
        response.setValue(request1);
        response.setRequestId("123456");

        String respText = helper.encode(response);
        System.out.println("resp text : " + respText);
        Response<Request> response1 = (Response<Request>) helper.decode(respText,new Response<Request>(){}.getClass());
        Request request2 = response1.getValue();
        System.out.println("value:" + request2.getMethodName());

    }

}
