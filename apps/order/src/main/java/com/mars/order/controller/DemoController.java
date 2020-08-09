package com.mars.order.controller;

import com.mars.common.model.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("")
    public ApiResult get(){
        return restTemplate.getForObject("http://system/api/sys/user/demo", ApiResult.class);
    }

}
