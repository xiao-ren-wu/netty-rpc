package org.ywb.rpc.register;

import org.ywb.rpc.core.RpcServiceMeta;

import java.io.IOException;

/**
 * @author yuwenbo1
 * @date 2021/2/17 11:54 上午 星期三
 * @since 1.0.0
 * 服务注册发现接口
 */
public interface RpcRegistryService {
    /**
     * 服务注册
     *
     * @param serviceMeta 服务描述
     * @throws Exception e
     */
    void register(RpcServiceMeta serviceMeta) throws Exception;

    /**
     * 服务取消注册
     *
     * @param serviceMeta 服务描述
     * @throws Exception e
     */
    void unRegister(RpcServiceMeta serviceMeta) throws Exception;

    /**
     * 获取服务描述
     *
     * @param serviceName     服务名称
     * @param serviceHashCode 服务hashCode
     * @return {@link RpcServiceMeta}
     * @throws Exception e
     */
    RpcServiceMeta discovery(String serviceName, int serviceHashCode) throws Exception;

    /**
     * 服务销毁
     *
     * @throws IOException IO
     */
    void destroy() throws IOException;
}
