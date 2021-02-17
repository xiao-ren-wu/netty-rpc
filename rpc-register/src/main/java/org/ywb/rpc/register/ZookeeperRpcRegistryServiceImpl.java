package org.ywb.rpc.register;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.ywb.rpc.core.NettyRpcServiceHelper;
import org.ywb.rpc.core.RpcServiceMeta;
import org.ywb.rpc.register.lb.ZKConsistentHashLoadBalancer;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @author yuwenbo1
 * @date 2021/2/17 11:59 上午 星期三
 * @since 1.0.0
 * zookeeper 服务注册实现
 */
public class ZookeeperRpcRegistryServiceImpl implements RpcRegistryService {

    public static final String RPC_SERVICE_BASE_PATH = "/netty-rpc";

    public static final int MAX_RETRY = 3;

    public static final int SLEEP_TIME = 1000;

    private final ServiceDiscovery<RpcServiceMeta> serviceDiscovery;

    public ZookeeperRpcRegistryServiceImpl(String registerAddr) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(registerAddr, new ExponentialBackoffRetry(SLEEP_TIME, MAX_RETRY));
        client.start();
        JsonInstanceSerializer<RpcServiceMeta> serializer = new JsonInstanceSerializer<>(RpcServiceMeta.class);

        serviceDiscovery = ServiceDiscoveryBuilder.builder(RpcServiceMeta.class)
                .client(client)
                .serializer(serializer)
                .basePath(RPC_SERVICE_BASE_PATH)
                .build();
        this.serviceDiscovery.start();
    }

    @Override
    public void register(RpcServiceMeta serviceMeta) throws Exception {
        ServiceInstance<RpcServiceMeta> serviceInstance = ServiceInstance
                .<RpcServiceMeta>builder()
                .name(NettyRpcServiceHelper.buildServiceKey(serviceMeta.getServiceName(), serviceMeta.getServiceVersion()))
                .address(serviceMeta.getServiceAddr())
                .port(serviceMeta.getPort())
                .payload(serviceMeta)
                .build();
        serviceDiscovery.registerService(serviceInstance);
    }

    @Override
    public void unRegister(RpcServiceMeta serviceMeta) throws Exception {
        ServiceInstance<RpcServiceMeta> serviceInstance = ServiceInstance
                .<RpcServiceMeta>builder()
                .name(serviceMeta.getServiceName())
                .address(serviceMeta.getServiceAddr())
                .port(serviceMeta.getPort())
                .payload(serviceMeta)
                .build();
        serviceDiscovery.unregisterService(serviceInstance);
    }

    @Override
    public RpcServiceMeta discovery(String serviceName, int serviceHashCode) throws Exception {
        Collection<ServiceInstance<RpcServiceMeta>> serviceInstances = serviceDiscovery.queryForInstances(serviceName);
        ServiceInstance<RpcServiceMeta> instance = new ZKConsistentHashLoadBalancer().select((List<ServiceInstance<RpcServiceMeta>>) serviceInstances, serviceHashCode);
        if (instance != null) {
            return instance.getPayload();
        }
        return null;
    }

    @Override
    public void destroy() throws IOException {
        serviceDiscovery.close();
    }
}
