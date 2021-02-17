package org.ywb.rpc.consumer;

import org.springframework.context.annotation.Bean;
import org.ywb.rpc.consumer.core.NettyRpcConsumer;
import org.ywb.rpc.consumer.proxy.NettyRpcCustomerBeanPostProcessor;

/**
 * @author yuwenbo1
 * @date 2021/2/17 8:09 下午 星期三
 * @since 1.0.0
 */
public class RpcConsumerConfiguration {

    @Bean
    public NettyRpcConsumer nettyRpcConsumer(){
        return new NettyRpcConsumer();
    }

    @Bean
    public NettyRpcCustomerBeanPostProcessor nettyRpcCustomerBeanPostProcessor(){
        return new NettyRpcCustomerBeanPostProcessor();
    }
}
