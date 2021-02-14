package org.ywb.rpc.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author yuwenbo1
 * @date 2021/2/14 10:44 上午 星期日
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "rpc")
public class NettyRpcProperties {

    private int servicePort;

    private String registerAddr;

    private String registerType;
}
