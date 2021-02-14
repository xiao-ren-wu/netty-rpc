package org.ywb.rpc.core;

import io.netty.util.concurrent.Promise;
import lombok.Data;

/**
 * @author yuwenbo1
 * @date 2021/2/14 10:30 上午 星期日
 * @since 1.0.0
 */
@Data
public class NettyRpcFuture<T> {

    private Promise<T> promise;

    private long timeout;

    public NettyRpcFuture(Promise<T> promise, long timeout) {
        this.promise = promise;
        this.timeout = timeout;
    }
}
