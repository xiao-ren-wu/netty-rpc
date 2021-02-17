package org.ywb.rpc.provider;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.ywb.rpc.core.RegistryType;

/**
 * @author yuwenbo1
 * @date 2021/2/14 10:44 上午 星期日
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "rpc")
public class NettyRpcProperties {

    @Value("${server.port:8080}")
    private int servicePort;

    private String registerAddr;

    private RegistryType registerType;
}
