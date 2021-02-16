package org.ywb.rpc.protocol.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.reflect.FastClass;
import org.ywb.rpc.core.NettyRpcRequest;
import org.ywb.rpc.core.NettyRpcResponse;
import org.ywb.rpc.core.NettyRpcServiceHelper;
import org.ywb.rpc.protocol.support.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;

/**
 * @author yuwenbo1
 * @date 2021/2/16 3:38 下午 星期二
 * @since 1.0.0
 */
@Slf4j
public class RpcRequestHandler extends SimpleChannelInboundHandler<NettyRpcProtocol<NettyRpcRequest>> {

    private Map<String, Object> serviceMap;

    public RpcRequestHandler(Map<String, Object> serviceMap) {
        this.serviceMap = serviceMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NettyRpcProtocol<NettyRpcRequest> requestNettyRpcProtocol) throws Exception {
        ThreadPoolExecutorHelper.submit(() -> {
            NettyRpcProtocol<NettyRpcResponse> responseNettyRpcProtocol = new NettyRpcProtocol<>();
            NettyRpcResponse nettyRpcResponse = new NettyRpcResponse();

            MsgHeader msgHeader = requestNettyRpcProtocol.getMsgHeader();
            try {
                msgHeader.setType((byte) MsgType.RESPONSE.getCode());
                // 反射调用
                Object result = handler(requestNettyRpcProtocol.getBody());
                nettyRpcResponse.setData(result);

                responseNettyRpcProtocol.setBody(nettyRpcResponse);
                responseNettyRpcProtocol.setMsgHeader(msgHeader);
            } catch (Exception e) {
                msgHeader.setStatus((byte) MsgStatus.FAIL.getCode());
                nettyRpcResponse.setMessage(e.toString());
                log.error("process request {} error", msgHeader.getMsgId(), e);
            }
            ctx.writeAndFlush(responseNettyRpcProtocol);
        });
    }

    private Object handler(NettyRpcRequest request) throws InvocationTargetException {
        String serviceKey = NettyRpcServiceHelper.buildServiceKey(request.getClassName(), request.getServiceVersion());
        Object serviceBean = serviceMap.get(serviceKey);
        if (Objects.isNull(serviceBean)) {
            throw new IllegalArgumentException(String.format("service not exist: %s:%s", request.getClassName(), request.getMethodName()));
        }
        Class<?> serviceBeanClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] params = request.getParams();

        FastClass fastClass = FastClass.create(serviceBeanClass);
        int methodIndex = fastClass.getIndex(methodName, parameterTypes);
        return fastClass.invoke(methodIndex, serviceBean, params);
    }
}




















