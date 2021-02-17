package org.ywb.rpc.register;

import org.ywb.rpc.core.RpcServiceMeta;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;

/**
 * @author yuwenbo1
 * @date 2021/2/17 12:26 下午 星期三
 * @since 1.0.0
 */
public class EurekaRegistryServiceImpl implements RpcRegistryService {

    public EurekaRegistryServiceImpl(String registryAddr) {
        throw new NotImplementedException();
    }

    @Override
    public void register(RpcServiceMeta serviceMeta) throws Exception {

    }

    @Override
    public void unRegister(RpcServiceMeta serviceMeta) throws Exception {

    }

    @Override
    public RpcServiceMeta discovery(String serviceName, int serviceHashCode) throws Exception {
        return null;
    }

    @Override
    public void destroy() throws IOException {

    }
}
