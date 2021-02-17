package org.ywb.rpc.protocol.support;

/**
 * @author yuwenbo1
 * @date 2021/2/16 1:30 下午 星期二
 * @since 1.0.0
 */
public interface ProtocolConstants {
    /**
     * 协议头长度
     */
    int HEADER_TOTAL_LEN = 18;
    /**
     * 魔数
     */
    short MAGIC = 0x10;
    /**
     * 协议版本
     */
    byte VERSION = 0x1;
}
