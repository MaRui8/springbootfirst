package com.mr.controller;


import com.mr.common.Cache;
import com.mr.mapper.UserMapper;
import com.mr.pojo.User;
import com.mr.task.CacheMessageSendTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @PostMapping("login")
    public ModelAndView login(HttpServletRequest request, String userName, String password) {
        User user = userMapper.selectUserByName(userName, password);
        ModelAndView modelAndView = new ModelAndView("index");
        if (null == user) {
            request.getSession().setAttribute("msg", "账户名与密码不匹配");
            return modelAndView;
        } else {
            byte type = judgeIsPc(request) ? (byte) 2 : (byte) 1;
            user.setBrowserType(type);
            Cache.addOnlineUser(userName, user);
            modelAndView.setViewName("main");
            modelAndView.addObject("userName", userName);
            request.getSession().setAttribute("userName", user);
            new Thread(new CacheMessageSendTask(userName)).start();
            return modelAndView;
        }
    }


    public boolean judgeIsPc(HttpServletRequest request) {
        if (request.getHeader("user-agent").contains("WOW64")) {
            return false;
        }
        return true;
    }

}
