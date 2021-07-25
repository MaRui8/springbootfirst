package com.mr.controller;


import com.mr.common.Cache;
import com.mr.common.Constant;
import com.mr.mapper.UserMapper;
import com.mr.pojo.User;
import com.mr.task.CacheMessageSendTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("user")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private UserMapper userMapper;

    @PostMapping("login")
    public ModelAndView login(HttpServletRequest request, String userName, String password) {
        User user = userMapper.selectUserByName(userName, password);
        ModelAndView modelAndView = new ModelAndView(new RedirectView("index"));
        if (null == user) {
            request.getSession().setAttribute("msg", "账户名与密码不匹配");
            return modelAndView;
        } else {
            byte type = judgeIsPc(request) ? (byte) 2 : (byte) 1;
            user.setBrowserType(type);
            Cache.addOnlineUser(userName, user);
            modelAndView.setViewName("main");
            modelAndView.addObject(Constant.HTTP_SESSION_ATTRIBUTE, userName);
            request.getSession().setAttribute(Constant.HTTP_SESSION_ATTRIBUTE, user);
            new Thread(new CacheMessageSendTask(userName)).start();
            return modelAndView;
        }
    }

    @GetMapping("getError")
    @ResponseBody
    public String getError() {
        try {
            int i = 1 / 0;
        } catch (Exception e) {
            logger.error("getError error", e);
            logger.info("getError error", e);
        }
        return "success";
    }


    public boolean judgeIsPc(HttpServletRequest request) {
        return !request.getHeader("user-agent").contains("WOW64");
    }

}
