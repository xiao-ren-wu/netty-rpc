package org.ywb.rpc.protocol.support;

import lombok.Getter;

import java.util.stream.Stream;

/**
 * @author yuwenbo1
 * @date 2021/2/16 11:28 上午 星期二
 * @since 1.0.0
 */
public enum MsgType {
    /**
     * req
     */
    REQUEST(1),
    /**
     * resp
     */
    RESPONSE(2),
    /**
     * heart beat
     */
    HEART_BEAT(3);

    @Getter
    private final int code;

    MsgType(int code) {
        this.code = code;
    }

    public static MsgType typeOf(byte type) {
        return Stream.of(MsgType.values())
                .filter(t -> t.code == type)
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
