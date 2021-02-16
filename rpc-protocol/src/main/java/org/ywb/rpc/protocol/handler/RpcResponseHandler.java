package org.ywb.rpc.protocol.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.ywb.rpc.core.NettyRpcFuture;
import org.ywb.rpc.core.NettyRpcHolder;
import org.ywb.rpc.core.NettyRpcResponse;
import org.ywb.rpc.protocol.support.NettyRpcProtocol;

/**
 * @author yuwenbo1
 * @date 2021/2/16 3:53 下午 星期二
 * @since 1.0.0
 */
public class RpcResponseHandler extends SimpleChannelInboundHandler<NettyRpcProtocol<NettyRpcResponse>> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, NettyRpcProtocol<NettyRpcResponse> nettyRpcResponseNettyRpcProtocol) throws Exception {
        long msgId = nettyRpcResponseNettyRpcProtocol.getMsgHeader().getMsgId();
        NettyRpcFuture<NettyRpcResponse> responseNettyRpcFuture = NettyRpcHolder.REQ_MAP.remove(msgId);
        responseNettyRpcFuture.getPromise().setSuccess(nettyRpcResponseNettyRpcProtocol.getBody());
    }
}
