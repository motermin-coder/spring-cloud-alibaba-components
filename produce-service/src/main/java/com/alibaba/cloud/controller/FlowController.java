package com.alibaba.cloud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auth tom
 * @Date 2023-04-06 21:44:10
 */
@RestController
@RequestMapping("/flow")
public class FlowController {


    @GetMapping("/b")
    public String testB(){
        return "b";
    }


    @GetMapping("/a")
    public String testA(){
        return "a";
    }
}
