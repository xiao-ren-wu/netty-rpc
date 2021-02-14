package org.ywb.rpc.core;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yuwenbo1
 * @date 2021/2/14 10:36 上午 星期日
 * @since 1.0.0
 */
@Data
public class NettyRpcRequest implements Serializable {

    private String serviceVersion;

    private String className;

    private String methodName;

    private Object[] params;

    private Class<?>[] parameterTypes;

}
