package utils;

import java.util.UUID;

public class UuidHelper {
    public static String uuid() {
        return UUID.randomUUID().toString();
    }
}
