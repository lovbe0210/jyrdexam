package com.jyrd.exam.home.service.impl;

import com.jyrd.exam.ucenter.service.UserService;
import com.jyrd.exam.base.entity.User;
import com.jyrd.exam.home.service.HomeService;
import com.jyrd.exam.base.common.utils.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private UserService userService;

    @Override
    public User login(String loginName, String pwd) {
        //校验数据有效性
        if(!ValidateUtil.isValid(loginName)){
            throw new RuntimeException("无法获取参数：loginName");
        }
        if(!ValidateUtil.isValid(pwd)){
            throw new RuntimeException("无法获取参数：pwd");
        }

        User user = userService.getUser(loginName);
        if(user == null || !user.getPassword().equals(pwd)){
            throw new RuntimeException("用户名或密码错误！");
        }

        return user;
    }
}
