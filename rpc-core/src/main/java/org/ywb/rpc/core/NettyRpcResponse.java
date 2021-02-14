package org.ywb.rpc.core;

import lombok.Data;

/**
 * @author yuwenbo1
 * @date 2021/2/14 10:41 上午 星期日
 * @since 1.0.0
 */
@Data
public class NettyRpcResponse {

    private Object data;

    private String message;
}
