package fastrpc.serialize;

import fastrpc.context.annotation.Bean;
import fastrpc.exception.UnSupportException;
import fastrpc.serialize.support.KryoUtils;

import java.lang.reflect.Type;

@Bean
public class KryoSerialize implements GeneralSerialize{
    @Override
    public <T> T decode(String json, Type type) {
        throw new UnSupportException("Json fastrpc.serialize unsupport params type: String, Type");
    }

    @Override
    public <T> T decode(byte[] bytes, Class clazz) {
        return (T) KryoUtils.deserialize(bytes, clazz);
    }

    @Override
    public String encode(Object orign) {
        throw new UnSupportException("Json fastrpc.serialize unsupport result type: String");
    }

    @Override
    public byte[] encodeToByte(Object orign) {
        return KryoUtils.serialize(orign);
    }



}
