package com.hope.mapper;

import com.hope.pojo.User;

import java.util.List;
import java.util.Map;


/**
 * 文件名：UserDao
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/10/15-19:17
 * 描述：
 */
public interface UserMapper {

    public User getById(int id);

    List<User> getuserByLimit(Map<String,Object> map);


}
