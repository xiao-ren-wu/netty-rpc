package org.ywb.rpc.provider.init;

import org.springframework.context.annotation.Bean;
import org.ywb.rpc.provider.NettyRpcProperties;
import org.ywb.rpc.provider.NettyRpcProvider;
import org.ywb.rpc.register.RegistryFactory;

import javax.annotation.Resource;

/**
 * @author yuwenbo1
 * @date 2021/2/17 4:37 下午 星期三
 * @since 1.0.0
 */
public class NettyProviderConfiguration {

    @Resource
    private NettyRpcProperties nettyRpcProperties;

    @Bean
    public NettyRpcProvider nettyRpcProvider() throws Exception {
        return new NettyRpcProvider(nettyRpcProperties.getServicePort(),
                RegistryFactory.getInstance(nettyRpcProperties.getRegisterAddr(), nettyRpcProperties.getRegisterType()));
    }

    @Bean
    public NettyRpcProperties nettyRpcProperties() {
        return new NettyRpcProperties();
    }
}
