package com.example.user.service;

import com.example.api.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author ziyuan
 * @Description
 * @createTime 2021年10月22日 22:33:00
 */
@DubboService(version = "1.0.0")
public class UserServiceImpl implements UserService {
    @Override
    public String login(String username) {
        return username;
    }
}
