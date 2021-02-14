package org.ywb.rpc.core;

/**
 * @author yuwenbo1
 * @date 2021/2/14 10:46 上午 星期日
 * @since 1.0.0
 */
public class NettyRpcServiceHelper {

    public static String buildServiceKey(String serviceName, String serviceVersion) {
        return String.join("#", serviceName, serviceVersion);
    }
}
