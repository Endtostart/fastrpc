package fastrpc.serialize;

import java.lang.reflect.Type;

public interface Decode {
    <T> T decode(String json, Type type);

    <T> T decode(byte[] bytes, Class clazz);
}

