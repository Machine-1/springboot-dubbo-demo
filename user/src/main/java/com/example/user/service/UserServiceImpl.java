package com.example.user.service;

import com.example.api.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * @author ziyuan
 * @Description
 * @createTime 2021年10月22日 22:33:00
 */
// api模块中定义了UserService，这里编写实现类
//    加上 @DubboService 注解标识该服务可以被远程调用
@DubboService(version = "1.0.0")
@Service
public class UserServiceImpl implements UserService {
    @Override
    public String login(String username) {
        return "UserServiceImpl" + username;
    }
}
