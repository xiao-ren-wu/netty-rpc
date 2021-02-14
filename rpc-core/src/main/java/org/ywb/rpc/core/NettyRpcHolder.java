package org.ywb.rpc.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author yuwenbo1
 * @date 2021/2/14 10:39 上午 星期日
 * @since 1.0.0
 */
public interface NettyRpcHolder {

    AtomicLong REQ_ID_GEN = new AtomicLong(0);

    Map<Long, NettyRpcFuture<NettyRpcResponse>> REQ_MAP = new ConcurrentHashMap<>();
}
