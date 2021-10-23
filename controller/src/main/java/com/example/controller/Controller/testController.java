package com.example.controller.Controller;

import com.example.api.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ziyuan
 * @Description
 * @createTime 2021年10月22日 23:26:00
 */
@RestController
public class testController {
    Logger logger = LoggerFactory.getLogger(testController.class);
    @DubboReference(version = "1.0.0",
    url = "dubbo://localhost:20880")
    private UserService userService;

    @GetMapping("/dubboTest/{msg}")
    public String method1(@PathVariable("msg") String msg) {
       return userService.login("controller give you a " + msg);
    }

    @GetMapping("/test")
    public String methodTest() {
        logger.error("done!!");
        return "success!!!";
    }
}


