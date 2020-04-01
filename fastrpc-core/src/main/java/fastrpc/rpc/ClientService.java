package fastrpc.rpc;

import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import fastrpc.context.annotation.Bean;
import fastrpc.context.annotation.Weave;
import fastrpc.context.support.ApplicationUtils;
import fastrpc.message.IRequest;
import fastrpc.message.IResponse;
import fastrpc.message.Response;
import fastrpc.netty.NettyClient;
import fastrpc.serialize.GeneralSerialize;
import fastrpc.serialize.JsonSerializeFactory;
import fastrpc.transport.jdknio.NioClient;
import fastrpc.utils.ThreadPoolUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Bean
public class ClientService<T> {

    Logger logger = LoggerFactory.getLogger(ClientService.class);
    @Weave
    private NettyClient client;
    @Weave
    private GeneralSerialize generalSerialize;
    private NioClient nioClient;

    private InheritableThreadLocal<byte[]> sendMsg = new InheritableThreadLocal();
    private InheritableThreadLocal<byte[]> resMsg = new InheritableThreadLocal();

    public void startClient() {
        nioClient = new NioClient(ApplicationUtils.getServiceHost(), ApplicationUtils.getServerPort()).addThreadLocal(sendMsg,resMsg).init();
    }

    public IResponse doCall(IRequest request) {
        logger.info("do calll >> request :");
        byte[] message = generalSerialize.encodeToByte(request);
        client.send(message);
        String result = "{\"interfaceName\":\"PublicClass\",\"methodName\":\"hello\"}";
        IResponse response = (IResponse) JsonSerializeFactory.getDecode().decode(result, IResponse.class);
        return response;
    }

    public IResponse doCall(IRequest request, Type type){
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
        return response;
    }

    // 网络请求
    public IResponse nioCall(IRequest request) {
        logger.info("nio request =.=!");
        IResponse response = null;
        byte[] message = generalSerialize.encodeToByte(request);
        /*sendMsg.remove();
        sendMsg.set(message);*/
        nioClient.setSendByte(message);

        /*
        ThreadPoolUtils.excute(()->{
            nioClient.send();});*/

       /* InheritableThreadLocal<byte[]> resThreadLocal = nioClient.getResMsg();
        while (true) {
            if (resThreadLocal.get() != null) {
                byte[] bytes = resThreadLocal.get();
                resThreadLocal.remove();
                response = generalSerialize.decode(bytes, Response.class);
                logger.info("循环结束: 返回response 结果 == >>" + response.toString());
                break;
            }
        }*/

        CompletableFuture<IResponse> completableFuture = CompletableFuture.supplyAsync(() -> {
            IResponse innerRes;
            while (true) {
                /*if (resMsg.get() != null) {
                    byte[] bytes = resMsg.get();
                    resMsg.remove();
                    innerRes = generalSerialize.decode(bytes, Response.class);
                    logger.info("循环结束: 返回response 结果 == >>" + innerRes.toString());
                    break;
                }*/
                byte[] res = nioClient.getResByte();
                if (res != null && res.length > 0) {
                    innerRes = generalSerialize.decode(res, Response.class);
                    logger.info("循环结束: 返回response 结果 == >>" + innerRes.toString());
                    break;
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return innerRes;
        });
        try {
            response = completableFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return response;
    }

}
