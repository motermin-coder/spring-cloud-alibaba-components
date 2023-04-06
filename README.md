### 1、Nacos的使用（注册中心、配置管理中心）
1、添加相关依赖
```xml
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-bootstrap</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
    </dependency>
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    </dependency>
```
2、bootstrap.yml 和 application.yml 添加相关配置
```yaml
##### bootstrap.yml
spring:
  application:
    name: produce-service # 服务名
  cloud:
    nacos:
      config:
        server-addr: 127.0.0.1:8848 # 配置中心的地址
        file-extension: yml # 配置文件的后缀名
```
```yaml
##### application.yml
server:
  port: 9002
spring:
  cloud:
    sentinel:
      transport:
        port: 8719
        dashboard: 127.0.0.1:8070
    loadbalancer:
      enabled: true
feign:
  sentinel:
    enabled: true
```

### 2、LoadBalancer的使用（客户端负载均衡）
1、添加相关依赖
```xml
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-loadbalancer</artifactId>
    </dependency>
```
2、添加相关配置
```java
@SpringBootApplication
@LoadBalancerClient(name = "produce-service",configuration = ConsumeServiceApplication.class)
public class ConsumeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumeServiceApplication.class,args);
    }

    /**
     * 选择轮询方式作为负载均衡策略
     * @param environment
     * @param serviceInstanceListSupplierProvider
     * @return
     */
    @Bean
    public ReactorLoadBalancer<ServiceInstance> loadBalancerClient(Environment environment,
                                                                   ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider){
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new RoundRobinLoadBalancer(serviceInstanceListSupplierProvider,name);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
```
3、使用restTemplate实现方法调用
```java
@RestController
public class ConsumeController {

    @Autowired
    private RestTemplate restTemplate;

    private static final String appName = "produce-service";
    
    @GetMapping("/port")
    public String port() {
        return restTemplate.getForObject(URI.create("http://" + appName + "/test"), String.class);
    }
}
```

### 3、OpenFeign的使用（服务调用）
1、添加相关依赖
```xml
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
```
2、添加相关注解
```java
@SpringBootApplication
@EnableFeignClients
public class ProduceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProduceServiceApplication.class,args);
    }
}
```

3、添加FeignClient接口（接口的里面的方法需要被调用的完全一致，比如：URL、方法名、参数、返回类型）
```java
@FeignClient(name = "consume-service")
public interface FeiClient {

    @GetMapping("/port")
    String port();

    @GetMapping("/test/Demo")
    String testDemo();
}
```

4、使用
```java
@RestController
public class ProduceController {
    
    @Autowired
    private FeiClient feiClient;
    
    @GetMapping("/testDemo")
    public String testDemo(){
        return feiClient.testDemo();
    }
}
```


### 4、Sentinel的使用（熔断与降级）
1、添加相关依赖
```xml
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
    </dependency>
```
2、在资源添加相关注解
```java
@RestController
public class ProduceController {
    
    
    /**
     * 资源名为product,可以在nacos或者在sentinel-dashboard配置相关限流规则
     * @param id
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/product/{id}")
    @SentinelResource(value = "product")
    public String product(@PathVariable Integer id, HttpServletRequest httpServletRequest){
        return "产品生产成功了，编号为" + id + ",生产者的端口号为：" + httpServletRequest.getRemotePort();
    }
}
```
3、在application.yml中配置sentinel-dashboard的路径及端口
```yaml
spring:
  cloud:
    sentinel:
      transport:
        port: 8719
        dashboard: 127.0.0.1:8070
```

4、启动sentinel-dashboard监控，执行如下命令
```shell
java -Dserver.port=8070 -Dcsp.sentinel.dashboard.server=localhost:8070 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.8.6.jar
```
