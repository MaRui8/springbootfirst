package com.mr.mapper;

import com.mr.pojo.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    @Select("SELECT * FROM user WHERE user_name = #{userName} AND password = #{password}")
    User selectUserByName(String userName, String password);

    @Select("SELECT * FROM user")
    List<User> selectAll();
}
