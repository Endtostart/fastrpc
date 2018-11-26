package fastrpc.rpc;

import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import fastrpc.context.annotation.Bean;
import fastrpc.context.annotation.Weave;
import fastrpc.message.IRequest;
import fastrpc.message.IResponse;
import fastrpc.message.Response;
import fastrpc.netty.NettyClient;
import fastrpc.serialize.GeneralSerialize;
import fastrpc.serialize.JsonSerializeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

@Bean
public class ClientService<T> {

    Logger logger = LoggerFactory.getLogger(ClientService.class);
    @Weave
    private NettyClient client;
    @Weave
    private GeneralSerialize generalSerialize;


    public IResponse doCall(IRequest request) {
        logger.info("do calll >> request :");
        byte[] message = generalSerialize.encodeToByte(request);
        client.send(message);
        String result = "{\"interfaceName\":\"PublicClass\",\"methodName\":\"hello\"}";
        IResponse response = (IResponse) JsonSerializeFactory.getDecode().decode(result, IResponse.class);
        return response;
    }

    public IResponse doCall(IRequest request, Type type) throws ClassNotFoundException {
        logger.info("do calll >> request :");
        byte[] message = generalSerialize.encodeToByte(request);
        byte[] result = client.send(message);
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

        //IResponse response = (IResponse) JsonSerializeFactory.getDecode().decode(result, resType);
        IResponse response = generalSerialize.decode(result, Response.class);

        /*IResponse response = new Response();
        response.setRequestId(request.getRequestId());
        response.setValue(value);*/
        return response;
    }




}
