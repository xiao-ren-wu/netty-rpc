package org.ywb.rpc.protocol.support;

import lombok.Getter;

/**
 * @author yuwenbo1
 * @date 2021/2/16 11:28 上午 星期二
 * @since 1.0.0
 */
public enum MsgStatus {
    /**
     * success
     */
    SUCCESS(0),
    /**
     * fail
     */
    FAIL(1)
    ;
    @Getter
    private final int code;

    MsgStatus(int code) {
        this.code = code;
    }
}
