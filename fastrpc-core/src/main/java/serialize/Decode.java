package serialize;

public interface Decode<T> {
    Object decode(String json, Class<T> clazz);
}

