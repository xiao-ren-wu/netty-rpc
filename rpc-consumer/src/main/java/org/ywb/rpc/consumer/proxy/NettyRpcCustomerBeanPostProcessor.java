package org.ywb.rpc.consumer.proxy;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.ywb.rpc.consumer.annos.RpcResource;
import org.ywb.rpc.core.NettyRpcConstants;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author yuwenbo1
 * @date 2021/2/17 6:39 下午 星期三
 * @since 1.0.0
 * 生成代理对象并交给Spring容器管理
 */
@Slf4j
public class NettyRpcCustomerBeanPostProcessor implements ApplicationContextAware, BeanClassLoaderAware, BeanFactoryPostProcessor {

    private ApplicationContext context;

    private ClassLoader classLoader;

    private final Map<String, BeanDefinition> rpcRefBeanDefinitions = new LinkedHashMap<>();

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
            String beanClassName = beanDefinition.getBeanClassName();
            if (beanClassName != null) {
                Class<?> resolveClassName = ClassUtils.resolveClassName(beanClassName, this.classLoader);
                ReflectionUtils.doWithFields(resolveClassName, this::parseRpcReference);
            }
        }

        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        this.rpcRefBeanDefinitions.forEach((beanName, beanDefinition) -> {
            if (context.containsBean(beanName)) {
                throw new IllegalArgumentException("spring context already has a bean named " + beanName);
            }
            registry.registerBeanDefinition(beanName, rpcRefBeanDefinitions.get(beanName));
            log.info("registered RpcReferenceBean {} success.", beanName);
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    private void parseRpcReference(Field field) {
        RpcResource annotation = AnnotationUtils.getAnnotation(field, RpcResource.class);
        if (annotation != null) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(RpcReferenceBean.class);
            // 调用 @see RpcReferenceBean#init()方法
            builder.setInitMethodName(NettyRpcConstants.INIT_METHOD_NAME);
            // 为RpcReferenceBean各个属性赋值
            builder.addPropertyValue("interfaceClass", field.getType());
            builder.addPropertyValue("serviceVersion", annotation.serviceVersion());
            builder.addPropertyValue("registryType", annotation.registryType());
            builder.addPropertyValue("registryAddr", annotation.registryAddress());
            builder.addPropertyValue("timeout", annotation.timeout());

            AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();

            rpcRefBeanDefinitions.put(field.getName(), beanDefinition);
        }
    }
}






















