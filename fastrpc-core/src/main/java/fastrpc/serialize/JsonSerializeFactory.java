package fastrpc.serialize;

import fastrpc.context.annotation.Bean;

@Bean
public class JsonSerializeFactory {

    private static DefaultJsonSerialize defaultJsonSerialize = null;

    public static Decode getDecode() {
        return getObject();
    }

    public static Encode getEncode() {
        return getObject();
    }

    private static DefaultJsonSerialize getObject() {
        if (defaultJsonSerialize == null) {
            return new DefaultJsonSerialize();
        }
        return defaultJsonSerialize;
    }

}
