package rpc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import message.IRequest;
import message.IResponse;
import message.Request;
import message.Response;
import netty.NettyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serialize.JsonSerializeFactory;

import java.lang.reflect.Type;

public class ClientService<T> {

    Logger logger = LoggerFactory.getLogger(ClientService.class);

    NettyClient client = new NettyClient();


    public IResponse doCall(IRequest request) {
        logger.info("do calll >> request :");
        String message = JsonSerializeFactory.getEncode().encode(request);
        logger.info(message);
        client.send(message);
        String result = "{\"interfaceName\":\"PublicClass\",\"methodName\":\"hello\"}";
        IResponse response = (IResponse) JsonSerializeFactory.getDecode().decode(result, IResponse.class);
        return response;
    }

    public IResponse doCall(IRequest request, Type clazz) throws ClassNotFoundException {
        logger.info("do calll >> request :");
        String message = JsonSerializeFactory.getEncode().encode(request);
        logger.info(message);
        client.send(message);
        String result = "{\"requestId\":\"123456\",\"value\":{\"interfaceName\":\"PublicClass\",\"methodName\":\"hello\"}}";
        //IResponse response = (IResponse<T>) JsonSerializeFactory.getDecode().decode(result, new Response<>(){}.getClass());
        IResponse response = (IResponse) JsonSerializeFactory.getDecode().decode(result,new Response<Request>(){}.getClass().getSuperclass());
        //IResponse response = (IResponse) JsonSerializeFactory.getDecode().decode(result, JSON.parseObject(message, clazz));
        return response;
    }




}
