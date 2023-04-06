package com.alibaba.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @Auth tom
 * @Date 2023-04-05 13:42:04
 */
@SpringBootApplication
@EnableDiscoveryClient
public class NacosServiceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(NacosServiceApplication.class, args);
        String property = run.getEnvironment().getProperty("user.name");
        System.out.println("property = " + property);
        String property1 = run.getEnvironment().getProperty("user.age");
        System.out.println("property1 = " + property1);
    }

}
