package fastrpc.serialize;

import com.alibaba.fastjson.JSON;
import fastrpc.context.annotation.Bean;
import fastrpc.exception.UnSupportException;

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

}
