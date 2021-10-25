package com.example.controller.Controller;

import com.example.api.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author ziyuan
 * @Description
 * @createTime 2021年10月22日 23:26:00
 */
@RestController
public class testController {

    Logger logger = LoggerFactory.getLogger(testController.class);

//    api模块中定义UserService
//    远程调用，相当于Spring Boot 中自动注入一个实现类
    @DubboReference(version = "1.0.0")
    private UserService userService;

//    测试服务功能
    @GetMapping("/dubboTest/{msg}")
    public String method1(@PathVariable("msg") String msg) {

        return userService.login( msg);
    }

}


