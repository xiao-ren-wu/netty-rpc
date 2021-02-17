package org.ywb.rpc.register;


import org.ywb.rpc.core.RegistryType;

/**
 * @author yuwenbo1
 * @date 2021/2/17 12:26 下午 星期三
 * @since 1.0.0
 */
public class RegistryFactory {

    private static volatile RpcRegistryService registryService;

    public static RpcRegistryService getInstance(String registryAddr, RegistryType type) throws Exception {

        if (null == registryService) {
            synchronized (RegistryFactory.class) {
                if (null == registryService) {
                    switch (type) {
                        case ZOOKEEPER:
                            registryService = new ZookeeperRpcRegistryServiceImpl(registryAddr);
                            break;
                        case EUREKA:
                            registryService = new EurekaRegistryServiceImpl(registryAddr);
                            break;
                        default:
                            throw new IllegalArgumentException(String.format("registry type is illegal %s", type));
                    }
                }
            }
        }
        return registryService;
    }
}
