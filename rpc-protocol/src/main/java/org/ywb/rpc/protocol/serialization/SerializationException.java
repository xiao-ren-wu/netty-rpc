package org.ywb.rpc.protocol.serialization;

import lombok.Getter;

/**
 * @author yuwenbo1
 * @date 2021/2/16 1:58 下午 星期二
 * @since 1.0.0
 */
@Getter
public class SerializationException extends RuntimeException {
    public SerializationException(String message) {
        super(message);
    }
}
