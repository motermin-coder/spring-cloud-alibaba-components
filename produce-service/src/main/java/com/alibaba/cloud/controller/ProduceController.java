package com.alibaba.cloud.controller;

import com.alibaba.cloud.client.FeiClient;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auth tom
 * @Date 2023-04-05 14:30:34
 */
@RestController
public class ProduceController {

    @Value("${server.port}")
    public String port;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private FeiClient feiClient;


    @GetMapping("/test/loadBalance")
    public String testLoadBalance(){
        ServiceInstance choose = loadBalancerClient.choose("consume-service");
        System.out.println("choose = " + choose);
        return "测试 loadBalance";
    }

    @GetMapping("/testDemo")
    public String testDemo(){
        return feiClient.testDemo();
    }

//    @GetMapping("/portTest")
//    public String portTest(){
//        return feiClient.port();
//    }


    @GetMapping("/test")
    @SentinelResource(value = "test")
    public String test(HttpServletRequest httpServletRequest){
        return "测试端口为：" + httpServletRequest.getRemotePort() + "请求的端口：" + port;
    }

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
