package org.ywb.rpc.register.lb;

import java.util.List;

/**
 * @author yuwenbo1
 * @date 2021/2/16 4:01 下午 星期二
 * @since 1.0.0
 */
public interface ServiceLoadBalancer<T> {

    /**
     * 从服务列表中选取一个实例
     *
     * @param services 服务列表
     * @param hashcode hashcode
     * @return services instance
     */
    T select(List<T> services, int hashcode);
}
