# netty-rpc

> use netty build normal rpc middleware

| 框架       | 版本         | 功能     |
| ---------- | ------------ | -------- |
| SpringBoot | 2.4.2        | 基础框架 |
| Netty      | 4.1.42.Final | 通讯框架 |
| Zookeeper  | 3.4.2        | 服务发现 |

### 使用指南
1. 服务提供方添加`prodiver`依赖
~~~xml
        <dependency>
            <groupId>org.ywb.rpc</groupId>
            <artifactId>rpc-provider</artifactId>
            <version>1.0.0</version>
        </dependency>
~~~
2. 服务消费方添加`consumer`依赖
~~~xml
        <dependency>
            <groupId>org.ywb.rpc</groupId>
            <artifactId>rpc-comsumer</artifactId>
            <version>1.0.0</version>
        </dependency>
~~~
3. 服务端配置rpc端口，以及zookeeper地址信息
~~~properties
rpc.register-addr=127.0.0.1:2181
rpc.register-type=zookeeper
rpc.service-port=8082
~~~
4. 发布服务
~~~java
@RpcService(serviceInterface = HelloWorldRpcService.class)
public class HelloWorldServiceImpl implements HelloWorldRpcService {
    @Override
    public void sayHello(String args) {
        System.out.println(args + " Hello ~");
    }
}
~~~
5. 消费服务
~~~java
    @RpcResource
    private HelloWorldRpcService helloWorldRpcService;

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            helloWorldRpcService.sayHello("Netty Rpc");
        };
    }
~~~
