package org.ywb.rpc.provider.annos;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yuwenbo1
 * @date 2021/2/17 3:48 下午 星期三
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface RpcService {

    Class<?> serviceInterface() default Object.class;

    String version() default "1.0";
}
