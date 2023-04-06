package com.alibaba.cloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * @Auth tom
 * @Date 2023-04-05 14:25:21
 */
@RestController
public class ConsumeController {

    @Autowired
    private RestTemplate restTemplate;

    private static final String appName = "produce-service";

    @GetMapping("/test/Demo")
    public String testDemo(){
        return "test Demo";
    }



    @GetMapping("/port")
    public String port(){
        return restTemplate.getForObject(URI.create("http://"+ appName +"/test"),String.class);
    }


    @GetMapping("/consume/{id}")
    public String consume(@PathVariable Integer id){
        String format = String.format("http://"+ appName +"/product/%s", id);
        System.out.println("format = " + format);
        return restTemplate.getForObject(URI.create(format),String.class);
    }
}
