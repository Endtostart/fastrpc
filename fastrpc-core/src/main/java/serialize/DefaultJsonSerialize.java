package serialize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import message.IRequest;
import message.IResponse;
import message.Request;
import message.Response;

public class DefaultJsonSerialize<T> extends AbstractJsonSerialize<T> {

    @Override
    public Object decode(String json, Class<T> clazz) {
        return JSON.parseObject(json,clazz);
    }

    @Override
    public String encode(Object orign) {
        return JSON.toJSONString(orign);
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
