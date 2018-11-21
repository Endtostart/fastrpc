package serialize;

import exception.UnSupportException;
import serialize.support.KryoUtils;

import java.lang.reflect.Type;

public class KryoSerialize implements GeneralSerialize{
    @Override
    public <T> T decode(String json, Type type) {
        throw new UnSupportException("Json serialize unsupport params type: String, Type");
    }

    @Override
    public <T> T decode(byte[] bytes, Class clazz) {
        return (T) KryoUtils.deserialize(bytes, clazz);
    }

    @Override
    public String encode(Object orign) {
        //TODO
        //return KryoUtils.serialize(orign);
        return null;
    }

}
