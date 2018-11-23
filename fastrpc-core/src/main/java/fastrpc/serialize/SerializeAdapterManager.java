package fastrpc.serialize;

import fastrpc.context.annotation.Bean;
import fastrpc.context.annotation.Weave;
import fastrpc.exception.UnSupportException;

import java.lang.reflect.Type;

@Bean(name = "generalSerialize")
public class SerializeAdapterManager implements GeneralSerialize {

    /**
     * 默认序列化工具 kryo
     */
    @Weave(name = "kryoSerialize")
    private GeneralSerialize delegation;

    public SerializeAdapterManager() {}

    public SerializeAdapterManager(GeneralSerialize delegation) {
        this.delegation = delegation;
    }

    @Override
    @Deprecated
    public <T> T decode(String json, Type type) {
        T res;
        try {
            res = delegation.decode(json, type);
        } catch (Exception e) {
            throw new UnSupportException(e.getMessage());
        }
        return res;
    }

    @Override
    public <T> T decode(byte[] bytes, Class clazz) {
        T res;
        try {
            res = delegation.decode(bytes, clazz);
        } catch (Exception e) {
            res = delegation.decode(new String(bytes), clazz);
        }
        return res;
    }

    @Override
    @Deprecated
    public String encode(Object orign) {
        String res;
        try {
            res = delegation.encode(orign);
        } catch (Exception e) {
            throw new UnSupportException(e.getMessage());
        }
        return res;
    }

    @Override
    public byte[] encodeToByte(Object orign) {
        return delegation.encodeToByte(orign);
    }
}
