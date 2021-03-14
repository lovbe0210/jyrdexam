package com.jyrd.exam.home.controller;

import com.jyrd.exam.base.entity.User;
import com.jyrd.exam.home.service.HomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@Slf4j
public class HomeController {

    @Autowired
    private HomeService homeService;


    @RequestMapping("/")
    public String index(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return "index/login";
        }else {
            request.setAttribute("userName",user.getLoginName());
            return "index/admin";
        }
    }

    @RequestMapping("/login")
    public String login(String loginName, String pwd, HttpServletRequest request){
        try {
            //完成登录
            User login = homeService.login(loginName, pwd);
            request.setAttribute("userName",login.getLoginName());
            request.getSession().setAttribute("user",login);

            // 跳转到管理员页面
            return "index/admin";
        } catch (Exception e) {
            log.error("完成登录错误：{}", e.getMessage());
            try {
                request.setAttribute("message", URLEncoder.encode(e.getMessage(), "UTF-8") );
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            return "redirect:/";
        }
    }


    @RequestMapping("/loginOut")
    public String loginOut(HttpServletRequest request){
            request.getSession().invalidate();
            return "redirect:/";
    }

}
