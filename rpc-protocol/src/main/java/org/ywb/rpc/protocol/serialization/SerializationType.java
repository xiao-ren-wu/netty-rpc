package org.ywb.rpc.protocol.serialization;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author yuwenbo1
 * @date 2021/2/16 2:11 下午 星期二
 * @since 1.0.0
 */
@Getter
public enum SerializationType {
    /**
     * GSON序列化
     */
    GSON(0x10),
    /**
     * hessian序列化
     */
    HESSIAN(0x11);

    private final int type;


    SerializationType(int type) {
        this.type = type;
    }

    public static SerializationType typeOf(byte byteType) {
        return Arrays.stream(SerializationType.values())
                .filter(type -> type.getType() == byteType)
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
