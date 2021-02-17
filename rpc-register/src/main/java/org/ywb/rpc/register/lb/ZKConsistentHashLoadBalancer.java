package org.ywb.rpc.register.lb;

import org.apache.curator.x.discovery.ServiceInstance;
import org.ywb.rpc.core.RpcServiceMeta;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author yuwenbo1
 * @date 2021/2/17 12:14 下午 星期三
 * @since 1.0.0
 */
public class ZKConsistentHashLoadBalancer implements ServiceLoadBalancer<ServiceInstance<RpcServiceMeta>>{

    private final static int VIRTUAL_NODE_SIZE = 10;

    private final static String VIRTUAL_NODE_SPLIT = "#";

    @Override
    public ServiceInstance<RpcServiceMeta> select(List<ServiceInstance<RpcServiceMeta>> services, int hashcode) {
        TreeMap<Integer, ServiceInstance<RpcServiceMeta>> ring = makeConsistentHashRing(services);
        return allocateNode(ring, hashcode);
    }

    private ServiceInstance<RpcServiceMeta> allocateNode(TreeMap<Integer, ServiceInstance<RpcServiceMeta>> ring, int hashCode) {
        Map.Entry<Integer, ServiceInstance<RpcServiceMeta>> entry = ring.ceilingEntry(hashCode);
        if (entry == null) {
            entry = ring.firstEntry();
        }
        return entry.getValue();
    }

    private TreeMap<Integer, ServiceInstance<RpcServiceMeta>> makeConsistentHashRing(List<ServiceInstance<RpcServiceMeta>> servers) {
        TreeMap<Integer, ServiceInstance<RpcServiceMeta>> ring = new TreeMap<>();
        for (ServiceInstance<RpcServiceMeta> instance : servers) {
            for (int i = 0; i < VIRTUAL_NODE_SIZE; i++) {
                ring.put((buildServiceInstanceKey(instance) + VIRTUAL_NODE_SPLIT + i).hashCode(), instance);
            }
        }
        return ring;
    }

    private String buildServiceInstanceKey(ServiceInstance<RpcServiceMeta> instance) {
        RpcServiceMeta payload = instance.getPayload();
        return String.join(":", payload.getServiceAddr(), String.valueOf(payload.getPort()));
    }
}
