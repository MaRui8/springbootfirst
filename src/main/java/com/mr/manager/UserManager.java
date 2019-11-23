package com.mr.manager;

import com.mr.common.Cache;
import com.mr.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class UserManager {

    @Autowired
    private UserMapper userMapper;

    @PostConstruct
    public void loadUser(){
        userMapper.selectAll().forEach(e->Cache.getExistUserNameList().add(e.getUserName()));
    }
}
