package com.hope.mapper;

import com.hope.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
/**
 * 文件名：UserDao
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/10/15-19:17
 * 描述：
 */
public interface UserMapper {

    @Select("select id, name, pwd from `user`")
    List<User> getUserList();

    @Select("select * from user where id=#{id}")
    User getUserById(@Param("id") int id);

    @Insert("insert into `user`(id, name, pwd) values (#{id},#{name},#{password})")
    int addUser(User user);

    @Update("update `user` set name=#{name},pwd=#{password} where id=#{id}")
    int updateUser(User user);

    @Delete("delete from `user` where id=#{uid}")
    int deleteUser(@Param("uid") int id);
}
