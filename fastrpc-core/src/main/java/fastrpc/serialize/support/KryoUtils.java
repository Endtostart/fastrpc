package fastrpc.serialize.support;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class KryoUtils {
    private static Kryo kryo = new Kryo();

    private KryoUtils() {
    }

    public static <T> byte[] serialize(T orign) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Output output = new Output(bos);
        kryo.writeObject(output, orign);
        output.flush();
        bytes = bos.toByteArray();
        return bytes;
    }

    public static <T> T deserialize(byte[] bytes, Class<T> clazz) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Input input = new Input(bis);
        T t = kryo.readObject(input, clazz);
        return t;
    }
}
