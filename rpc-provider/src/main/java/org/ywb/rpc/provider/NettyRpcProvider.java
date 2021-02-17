package org.ywb.rpc.provider;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.ywb.rpc.core.NettyRpcServiceHelper;
import org.ywb.rpc.core.RpcServiceMeta;
import org.ywb.rpc.protocol.codec.NettyRpcDecoder;
import org.ywb.rpc.protocol.codec.NettyRpcEncoder;
import org.ywb.rpc.protocol.handler.RpcRequestHandler;
import org.ywb.rpc.provider.annos.RpcService;
import org.ywb.rpc.register.RpcRegistryService;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yuwenbo1
 * @date 2021/2/17 3:50 下午 星期三
 * @since 1.0.0
 */
@Slf4j
public class NettyRpcProvider implements InitializingBean, BeanPostProcessor {

    private String serverAddress;

    private final int serverPort;

    private final RpcRegistryService registryService;

    private final Map<String, Object> rpcServiceMap = new HashMap<>();

    public NettyRpcProvider(int serverPort, RpcRegistryService registryService) {
        this.serverPort = serverPort;
        this.registryService = registryService;
    }

    @Override
    @SuppressWarnings("all")
    public void afterPropertiesSet() throws Exception {
        new Thread(() -> {
            try {
                startServer();
            } catch (UnknownHostException | InterruptedException e) {
                log.error("start rpc server failed.", e);
            }
        }, "rpc-server").start();
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
        if (rpcService != null) {
            String serviceName = rpcService.serviceInterface().getName();
            String version = rpcService.version();

            RpcServiceMeta serviceMeta = RpcServiceMeta.builder()
                    .port(serverPort)
                    .serviceAddr(serverAddress)
                    .serviceName(serviceName)
                    .serviceVersion(version)
                    .build();
            try {
                registryService.register(serviceMeta);
                rpcServiceMap.put(NettyRpcServiceHelper.buildServiceKey(serviceName, version), bean);
            } catch (Exception e) {
                log.error("failed to register service {}#{}", serviceName, version, e);
            }
        }
        return bean;
    }

    private void startServer() throws UnknownHostException, InterruptedException {
        this.serverAddress = InetAddress.getLocalHost().getHostAddress();

        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class)
                    .group(bossGroup, workerGroup)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                            nioSocketChannel.pipeline()
                                    .addLast(new NettyRpcEncoder())
                                    .addLast(new NettyRpcDecoder())
                                    .addLast(new RpcRequestHandler(rpcServiceMap));
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture channelFuture = serverBootstrap.bind(serverPort).sync();
            log.info("server addr {} started on port {}", this.serverAddress, this.serverPort);
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}