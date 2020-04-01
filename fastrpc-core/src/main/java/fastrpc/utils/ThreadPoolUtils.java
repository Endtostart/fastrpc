package fastrpc.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPoolUtils {
    static ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    public static void excute(Runnable runnable) {
        threadPoolExecutor.execute(runnable);
    }

    public static void shutdown() {
        threadPoolExecutor.shutdown();
    }
}
