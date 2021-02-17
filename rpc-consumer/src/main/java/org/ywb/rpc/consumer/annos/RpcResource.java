package org.ywb.rpc.consumer.annos;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yuwenbo1
 * @date 2021/2/17 4:52 下午 星期三
 * @since 1.0.0
 */
@Autowired
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RpcResource {

    String serviceVersion() default "1.0";

    String registryType() default "ZOOKEEPER";

    String registryAddress() default "127.0.0.1:2181";

    long timeout() default 5000;
}
