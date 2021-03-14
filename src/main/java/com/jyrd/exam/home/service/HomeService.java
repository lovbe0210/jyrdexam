package com.jyrd.exam.home.service;

import com.jyrd.exam.base.entity.User;

public interface HomeService {

    User login(String loginName, String pwd);
}
