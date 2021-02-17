package org.ywb.rpc.consumer.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.ywb.rpc.consumer.annos.RpcResource;
import org.ywb.rpc.facade.HelloWorldRpcService;

@SpringBootApplication
public class RpcConsumerTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcConsumerTestApplication.class, args);
    }

    @RpcResource
    private HelloWorldRpcService helloWorldRpcService;

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            helloWorldRpcService.sayHello("Netty Rpc");
        };
    }
}
