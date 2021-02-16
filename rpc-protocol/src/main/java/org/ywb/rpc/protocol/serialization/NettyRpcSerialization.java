package org.ywb.rpc.protocol.serialization;

/**
 * @author yuwenbo1
 * @date 2021/2/16 1:38 下午 星期二
 * @since 1.0.0
 * 序列化接口
 */
public interface NettyRpcSerialization {
    /**
     * 序列化对象为byte array
     *
     * @param obj obj
     * @return byte[]
     */
    byte[] serialize(Object obj);

    /**
     * 反序列化对象
     *
     * @param bytes  对象字节数组
     * @param tClass 对象类型
     * @param <T>    T
     * @return t
     */
    <T> T deserialize(byte[] bytes, Class<T> tClass);
}
