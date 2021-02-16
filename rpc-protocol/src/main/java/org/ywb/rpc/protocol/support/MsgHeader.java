package org.ywb.rpc.protocol.support;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yuwenbo1
 * @date 2021/2/16 11:27 上午 星期二
 * @since 1.0.0
 * 协议头
 * +---------------------------------------------------------------+
 * | 魔数 2byte | 协议版本号 1byte | 序列化算法 1byte | 报文类型 1byte  |
 * +---------------------------------------------------------------+
 * | 状态 1byte |        消息 ID 8byte     |      数据长度 4byte     |
 * +---------------------------------------------------------------+
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MsgHeader {
    /**
     * 魔数
     */
    private short magic;
    /**
     * 版本号
     */
    private byte version;
    /**
     * 序列化算法
     */
    private byte serialization;
    /**
     * 报文类型
     */
    private byte type;
    /**
     * 状态
     */
    private byte status;
    /**
     * 消息id
     */
    private long msgId;
    /**
     * 数据长度
     */
    private int msgLen;
}
