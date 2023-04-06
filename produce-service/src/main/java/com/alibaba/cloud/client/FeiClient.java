package com.alibaba.cloud.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Auth tom
 * @Date 2023-04-06 22:17:30
 */
@FeignClient(name = "consume-service")
public interface FeiClient {

    @GetMapping("/port")
    String port();

    @GetMapping("/test/Demo")
    String testDemo();
}
