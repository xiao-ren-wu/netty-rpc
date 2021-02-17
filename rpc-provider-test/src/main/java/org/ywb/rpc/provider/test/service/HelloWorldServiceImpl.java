package org.ywb.rpc.provider.test.service;

import org.ywb.rpc.facade.HelloWorldRpcService;
import org.ywb.rpc.provider.annos.RpcService;

/**
 * @author yuwenbo1
 * @date 2021/2/17 7:50 下午 星期三
 * @since 1.0.0
 */
@RpcService(serviceInterface = HelloWorldRpcService.class)
public class HelloWorldServiceImpl implements HelloWorldRpcService {
    @Override
    public void sayHello(String args) {
        System.out.println(args + " Hello ~");
    }
}
