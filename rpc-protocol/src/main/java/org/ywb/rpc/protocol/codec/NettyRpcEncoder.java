package org.ywb.rpc.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.ywb.rpc.protocol.serialization.SerializationFactory;
import org.ywb.rpc.protocol.support.MsgHeader;
import org.ywb.rpc.protocol.support.NettyRpcProtocol;
import org.ywb.rpc.protocol.support.ProtocolConstants;

/**
 * @author yuwenbo1
 * @date 2021/2/16 11:26 上午 星期二
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
public class NettyRpcEncoder extends MessageToByteEncoder<NettyRpcProtocol<Object>> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, NettyRpcProtocol<Object> objectNettyRpcProtocol, ByteBuf byteBuf) throws Exception {
        MsgHeader msgHeader = objectNettyRpcProtocol.getMsgHeader();
        byteBuf.writeShort(ProtocolConstants.MAGIC);
        byteBuf.writeByte(msgHeader.getVersion());
        byte serialization = msgHeader.getSerialization();
        byteBuf.writeByte(serialization);
        byteBuf.writeByte(msgHeader.getType());
        byteBuf.writeByte(msgHeader.getStatus());
        byteBuf.writeLong(msgHeader.getMsgId());
        Object body = objectNettyRpcProtocol.getBody();
        byte[] bytes = SerializationFactory.getRpcSerialization(serialization).serialize(body);
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }
}
















