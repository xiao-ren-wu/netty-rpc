package org.ywb.rpc.protocol.serialization;

/**
 * @author yuwenbo1
 * @date 2021/2/16 2:10 下午 星期二
 * @since 1.0.0
 */
public class SerializationFactory {

    /**
     * 通过序列化标志查找对应的序列化算法
     *
     * @param byteType byte of serialization type
     * @return NettyRpcSerialization
     */
    public static NettyRpcSerialization getRpcSerialization(byte byteType) {
        SerializationType type = SerializationType.typeOf(byteType);
        switch (type) {
            case GSON:
                return GsonRpcSerialization.INSTANCE;
            case HESSIAN:
                return HessianRpcSerialization.INSTANCE;
            default:
                throw new IllegalArgumentException("serialization type [" + type + "] not found");
        }
    }
}
