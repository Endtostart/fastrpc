package rpc;

import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import message.IRequest;
import message.IResponse;
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

    public IResponse doCall(IRequest request, Type type) throws ClassNotFoundException {
        logger.info("do calll >> request :");
        String message = JsonSerializeFactory.getEncode().encode(request);
        logger.info(message);
        String result = client.send(message);
        //String result = "{\"requestId\":\"123456\",\"value\":{\"interfaceName\":\"PublicClass\",\"methodName\":\"hello\"}}";

        /**
         *
         * TODO
         * 因为调用返回 IResponse<T> 需要根据 T 的具体类型json反序列化
         * 传入的参数 type 为通过反射拿到的方法返回类型 也就对应的是T的类型
         * 这里封装一个 反序列需要的 Type类型 利用ParameterizedTypeImpl封装
         * 当然应该可以有其他的更优雅，健壮的方法
         *
         * 需要考虑 异常 和 无返回的情况
         *
         */
        Type resType = new ParameterizedTypeImpl(new Type[]{type}, null, Response.class);

        IResponse response = (IResponse) JsonSerializeFactory.getDecode().decode(result, resType);
        return response;
    }




}
