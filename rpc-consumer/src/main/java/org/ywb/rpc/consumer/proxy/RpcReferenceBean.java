package org.ywb.rpc.consumer.proxy;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.ywb.rpc.core.RegistryType;
import org.ywb.rpc.register.RegistryFactory;
import org.ywb.rpc.register.RpcRegistryService;

import java.lang.reflect.Proxy;

/**
 * @author yuwenbo1
 * @date 2021/2/17 5:06 下午 星期三
 * @since 1.0.0
 * 用于生成动态代理bean
 */
@Setter
@Slf4j
public class RpcReferenceBean implements FactoryBean<Object> {

    private Class<?> interfaceClass;

    private String serviceVersion;

    private RegistryType registryType;

    private String registryAddr;

    private long timeout;

    private Object object;

    /**
     * 会通过反射调用该方法生成代理bean
     */
    @SuppressWarnings("unused")
    public void init() throws Exception {
        log.info("init....");
        RpcRegistryService registryService = RegistryFactory.getInstance(registryAddr, registryType);
        this.object = Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new RpcMethodInvokeProxy(serviceVersion, timeout, registryService)
        );
    }

    @Override
    public Object getObject() throws Exception {
        return object;
    }

    @Override
    public Class<?> getObjectType() {
        return this.interfaceClass;
    }
}
