package com.alibaba.cloud;

import com.alibaba.cloud.circuitbreaker.sentinel.SentinelCircuitBreakerFactory;
import com.alibaba.cloud.circuitbreaker.sentinel.SentinelConfigBuilder;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * @Auth tom
 * @Date 2023-04-05 14:21:23
 */
@SpringBootApplication
@EnableFeignClients
@LoadBalancerClient(name = "consume-service",configuration = ProduceServiceApplication.class)
public class ProduceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProduceServiceApplication.class,args);
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

//    @Bean
//    public Customizer<SentinelCircuitBreakerFactory> customizer(){
//        return factory -> {
//            factory.configureDefault(id -> new SentinelConfigBuilder()
//                    .resourceName(id)
//                    .rules(Collections.singletonList(new DegradeRule(id)
//                    .setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT)
//                    .setCount(3)
//                    .setTimeWindow(10)))
//                    .build()
//            );
//        };
//    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
