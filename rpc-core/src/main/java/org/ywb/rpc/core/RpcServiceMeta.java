package org.ywb.rpc.core;

import lombok.Data;

/**
 * @author yuwenbo1
 * @date 2021/2/14 10:48 上午 星期日
 * @since 1.0.0
 */
@Data
public class RpcServiceMeta {

    private String serviceName;

    private String serviceVersion;

    private String serviceAddr;

    private int port;
}
