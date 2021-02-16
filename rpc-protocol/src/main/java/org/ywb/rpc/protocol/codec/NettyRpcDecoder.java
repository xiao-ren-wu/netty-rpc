package org.ywb.rpc.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.ywb.rpc.core.NettyRpcRequest;
import org.ywb.rpc.core.NettyRpcResponse;
import org.ywb.rpc.protocol.serialization.NettyRpcSerialization;
import org.ywb.rpc.protocol.serialization.SerializationFactory;
import org.ywb.rpc.protocol.support.MsgHeader;
import org.ywb.rpc.protocol.support.MsgType;
import org.ywb.rpc.protocol.support.NettyRpcProtocol;
import org.ywb.rpc.protocol.support.ProtocolConstants;

import java.util.List;
import java.util.Objects;

/**
 * @author yuwenbo1
 * @date 2021/2/16 11:25 上午 星期二
 * @since 1.0.0
 * 传输协议内容
 * +---------------------------------------------------------------+
 * | 魔数 2byte | 协议版本号 1byte | 序列化算法 1byte | 报文类型 1byte  |
 * +---------------------------------------------------------------+
 * | 状态 1byte |        消息 ID 8byte     |      数据长度 4byte     |
 * +---------------------------------------------------------------+
 * |                    数据内容(长度不定)                           |
 * +---------------------------------------------------------------+
 */
public class NettyRpcDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int i = byteBuf.readableBytes();
        if (i < ProtocolConstants.HEADER_TOTAL_LEN) {
            return;
        }
        // 校验魔数
        short magic = byteBuf.readShort();
        if (magic != ProtocolConstants.MAGIC) {
            throw new IllegalArgumentException("magic number is illegal " + magic);
        }
        MsgHeader msgHeader = genHeader(byteBuf);
        byte[] bytes = new byte[msgHeader.getMsgLen()];
        byteBuf.readBytes(bytes);

        MsgType msgType = MsgType.typeOf(msgHeader.getType());
        NettyRpcSerialization rpcSerialization = SerializationFactory.getRpcSerialization(msgHeader.getSerialization());
        Object body = null;
        switch (msgType) {
            case REQUEST:
                body = rpcSerialization.deserialize(bytes, NettyRpcRequest.class);
                break;
            case RESPONSE:
                body = rpcSerialization.deserialize(bytes, NettyRpcResponse.class);
            default:
                // heart beat
                break;
        }
        if (Objects.nonNull(body)) {
            NettyRpcProtocol<Object> nettyRpcProtocol = new NettyRpcProtocol<>();
            nettyRpcProtocol.setMsgHeader(msgHeader);
            nettyRpcProtocol.setBody(body);
            list.add(nettyRpcProtocol);
        }
    }

    private MsgHeader genHeader(ByteBuf byteBuf) {
        return MsgHeader.builder()
                .version(byteBuf.readByte())
                .serialization(byteBuf.readByte())
                .type(byteBuf.readByte())
                .status(byteBuf.readByte())
                .msgId(byteBuf.readLong())
                .msgLen(byteBuf.readInt())
                .build();
    }
}










