package fastrpc.context.register;

public interface Register {
    <T> T register(String name, Class<T> target);

    void register(String packagePath);
}
