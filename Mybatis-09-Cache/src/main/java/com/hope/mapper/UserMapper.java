package com.hope.mapper;

import com.hope.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文件名：UserMapper
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/10/17-15:15
 * 描述：
 */
public interface UserMapper {
        public  List<User> quertUserById(@Param("id") int id);


        int updateUser(User user);

}
