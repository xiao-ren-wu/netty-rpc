package org.ywb.rpc.consumer.proxy;

import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import org.ywb.rpc.consumer.core.NettyRpcConsumer;
import org.ywb.rpc.core.NettyRpcFuture;
import org.ywb.rpc.core.NettyRpcHolder;
import org.ywb.rpc.core.NettyRpcRequest;
import org.ywb.rpc.core.NettyRpcResponse;
import org.ywb.rpc.protocol.serialization.SerializationType;
import org.ywb.rpc.protocol.support.*;
import org.ywb.rpc.register.RpcRegistryService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author yuwenbo1
 * @date 2021/2/17 4:58 下午 星期三
 * @since 1.0.0
 */
public class RpcMethodInvokeProxy implements InvocationHandler {

    private final String serviceVersion;

    private final long timeout;

    private final RpcRegistryService registryService;

    public RpcMethodInvokeProxy(String serviceVersion, long timeout, RpcRegistryService registryService) {
        this.serviceVersion = serviceVersion;
        this.timeout = timeout;
        this.registryService = registryService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        NettyRpcProtocol<NettyRpcRequest> requestNettyRpcProtocol = genReqProtocol(method, args);
        // 建立和服务端的链接，
        NettyRpcConsumer nettyRpcConsumer = new NettyRpcConsumer();
        nettyRpcConsumer.sendRequest(requestNettyRpcProtocol, registryService);

        // 包装请求并存储到请求map中
        NettyRpcFuture<NettyRpcResponse> nettyRpcFuture = new NettyRpcFuture<>(new DefaultPromise<>(new DefaultEventLoop()), timeout);
        NettyRpcHolder.REQ_MAP.put(requestNettyRpcProtocol.getMsgHeader().getMsgId(), nettyRpcFuture);

        // 发送rpc请求
        nettyRpcConsumer.sendRequest(requestNettyRpcProtocol, registryService);

        // 获取请求
        return nettyRpcFuture.getPromise().get(timeout, TimeUnit.MILLISECONDS).getData();
    }

    private NettyRpcProtocol<NettyRpcRequest> genReqProtocol(Method method, Object[] args) {
        NettyRpcProtocol<NettyRpcRequest> requestNettyRpcProtocol = new NettyRpcProtocol<>();

        NettyRpcRequest request = NettyRpcRequest.builder()
                .methodName(method.getName())
                .className(method.getDeclaringClass().getName())
                .parameterTypes(method.getParameterTypes())
                .params(args)
                .serviceVersion(serviceVersion)
                .build();

        requestNettyRpcProtocol.setBody(request);

        MsgHeader header = MsgHeader.builder()
                .msgId(NettyRpcHolder.REQ_ID_GEN.getAndIncrement())
                .status((byte) MsgStatus.SUCCESS.getCode())
                .type((byte) MsgType.REQUEST.getCode())
                .serialization((byte) SerializationType.HESSIAN.getType())
                .version(ProtocolConstants.VERSION)
                .magic(ProtocolConstants.MAGIC)
                .build();
        requestNettyRpcProtocol.setMsgHeader(header);

        return requestNettyRpcProtocol;
    }
}













