package org.ywb.rpc.consumer.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.ywb.rpc.core.NettyRpcRequest;
import org.ywb.rpc.core.NettyRpcServiceHelper;
import org.ywb.rpc.core.RpcServiceMeta;
import org.ywb.rpc.protocol.codec.NettyRpcDecoder;
import org.ywb.rpc.protocol.codec.NettyRpcEncoder;
import org.ywb.rpc.protocol.handler.RpcResponseHandler;
import org.ywb.rpc.protocol.support.NettyRpcProtocol;
import org.ywb.rpc.register.RpcRegistryService;

import java.util.Objects;

/**
 * @author yuwenbo1
 * @date 2021/2/17 4:53 下午 星期三
 * @since 1.0.0
 */
@Slf4j
public class NettyRpcConsumer {

    private final Bootstrap bootstrap;

    private final EventLoopGroup eventLoopGroup;

    public NettyRpcConsumer() {
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup(4);
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline()
                                .addLast(new NettyRpcEncoder())
                                .addLast(new NettyRpcDecoder())
                                .addLast(new RpcResponseHandler());
                    }
                });
    }

    public void sendRequest(NettyRpcProtocol<NettyRpcRequest> protocol, RpcRegistryService registryService) throws Exception {
        NettyRpcRequest rpcRequest = protocol.getBody();
        Object[] params = rpcRequest.getParams();
        int hashCode = params[0].hashCode();
        String serviceKey = NettyRpcServiceHelper.buildServiceKey(rpcRequest.getClassName(), rpcRequest.getServiceVersion());
        RpcServiceMeta serviceMeta = registryService.discovery(serviceKey, hashCode);
        if (Objects.nonNull(serviceMeta)) {
            ChannelFuture future = bootstrap.connect(serviceMeta.getServiceAddr(), serviceMeta.getPort())
                    .sync();
            future.addListener((ChannelFutureListener) f -> {
                if (f.isSuccess()) {
                    log.info("connect server {},port {} success", serviceMeta.getServiceAddr(), serviceMeta.getPort());
                } else {
                    log.error("connect server {},port {} failed", serviceMeta.getServiceAddr(), serviceMeta.getPort());
                    future.cause().printStackTrace();
                    eventLoopGroup.shutdownGracefully();
                }
            });
            future.channel().writeAndFlush(protocol);
        }
    }
}













