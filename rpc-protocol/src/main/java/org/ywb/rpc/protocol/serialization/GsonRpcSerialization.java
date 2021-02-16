package org.ywb.rpc.protocol.serialization;

import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author yuwenbo1
 * @date 2021/2/16 1:43 下午 星期二
 * @since 1.0.0
 * GSON 序列化
 */
public class GsonRpcSerialization implements NettyRpcSerialization {

    private GsonRpcSerialization() {
    }

    public static final NettyRpcSerialization INSTANCE = new GsonRpcSerialization();

    private static final Gson GSON = new Gson();

    @Override
    public byte[] serialize(Object obj) {
        return GSON.toJson(obj).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> tClass) {
        if (Objects.isNull(bytes) || bytes.length == 0) {
            throw new NullPointerException();
        }
        return GSON.fromJson(new String(bytes, StandardCharsets.UTF_8), tClass);
    }
}
