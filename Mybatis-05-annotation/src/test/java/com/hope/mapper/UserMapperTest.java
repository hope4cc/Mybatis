package com.hope.mapper;

import com.hope.pojo.User;
import com.hope.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;


/**
 * 文件名：UserDaoTest
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/10/15-19:27
 * 描述：
 */
public class UserMapperTest {

     static Logger logger = Logger.getLogger(UserMapperTest.class);

    @Test
    public void getUserList() {
        SqlSession sqlSession = null;
        try {
            sqlSession = MybatisUtils.getSqlSession();
            //底层主要应用反射
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<User> userList = userMapper.getUserList();
            for (User user : userList) {
                System.out.println(user.toString());
            }
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }



    @Test
    public void getUserById() {
        SqlSession sqlSession = null;
        try {
            sqlSession = MybatisUtils.getSqlSession();
            //底层主要应用反射
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            User id = userMapper.getUserById(2);
            System.out.println(id);
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }


    @Test
    public void addUser() {
        SqlSession sqlSession = null;
        try {
            sqlSession = MybatisUtils.getSqlSession();
            //底层主要应用反射
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            int count = userMapper.addUser(new User(8, "zyy6", "123456"));
            System.out.println(count);
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }

    @Test
    public void updateUser() {
        SqlSession sqlSession = null;
        try {
            sqlSession = MybatisUtils.getSqlSession();
            //底层主要应用反射
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            int count = userMapper.updateUser(new User(6, "zyy2", "123123"));
            System.out.println(count);
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }

    @Test
    public void deleteUser() {
        SqlSession sqlSession = null;
        try {
            sqlSession = MybatisUtils.getSqlSession();
            //底层主要应用反射
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            int count = userMapper.deleteUser(5);
            System.out.println(count);
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }

}

