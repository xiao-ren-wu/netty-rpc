package org.ywb.rpc.protocol.serialization;

import com.caucho.hessian.io.HessianSerializerInput;
import com.caucho.hessian.io.HessianSerializerOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * @author yuwenbo1
 * @date 2021/2/16 1:49 下午 星期二
 * @since 1.0.0
 */
public class HessianRpcSerialization implements NettyRpcSerialization {

    private HessianRpcSerialization() {
    }

    public static final NettyRpcSerialization INSTANCE = new HessianRpcSerialization();

    @Override
    public byte[] serialize(Object obj) {
        if (Objects.isNull(obj)) {
            throw new NullPointerException();
        }
        HessianSerializerOutput hessianSerializerOutput;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            hessianSerializerOutput = new HessianSerializerOutput(bos);
            hessianSerializerOutput.writeObject(obj);
            hessianSerializerOutput.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new SerializationException(e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialize(byte[] bytes, Class<T> tClass) {
        if (Objects.isNull(bytes) || bytes.length == 0) {
            throw new NullPointerException();
        }
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
            return (T) new HessianSerializerInput(bis).readObject(tClass);
        } catch (IOException e) {
            throw new SerializationException(e.getMessage());
        }
    }
}
