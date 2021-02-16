package org.ywb.rpc.protocol.support;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author yuwenbo1
 * @date 2021/2/16 3:16 下午 星期二
 * @since 1.0.0
 * 线程池主助手
 */
public class ThreadPoolExecutorHelper {

    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR;

    private static final AtomicLong THREAD_ID = new AtomicLong(0);

    static {
        THREAD_POOL_EXECUTOR =
                new ThreadPoolExecutor(
                        16,
                        32,
                        60L,
                        TimeUnit.SECONDS,
                        new ArrayBlockingQueue<>(1000),
                        r -> new Thread(r, "netty-rpc-thread-" + THREAD_ID.getAndIncrement()));
    }

    public static void submit(Runnable task) {
        THREAD_POOL_EXECUTOR.execute(task);
    }
}
