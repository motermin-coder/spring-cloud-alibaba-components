package com.alibaba.cloud;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

/**
 * @Auth tom
 * @Date 2023-04-05 14:14:53
 */
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
