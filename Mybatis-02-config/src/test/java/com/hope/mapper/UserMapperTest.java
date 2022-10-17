package com.hope.mapper;

import com.hope.pojo.User;
import com.hope.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
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
    @Test
    public void getAllTest(){
        // 1、 获得SqlSession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try {
            //方式一：getMapper
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            List<User> userList = mapper.getAll();
            for (User user : userList) {
                System.out.println(user);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            //关闭sqlSession
            sqlSession.close();
        }
    }

    @Test
    public void getByIdTest(){
        // 1、 获得SqlSession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try {
            //获得getMapper接口对象
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            User byId = mapper.getById(1);
            System.out.println(byId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            //关闭sqlSession
            sqlSession.close();
        }
    }

    @Test
    public void useraddTest(){
        // 1、 获得SqlSession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();
            //获得getMapper接口对象
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        int i = mapper.useradd(new User(7, "菜菜", "123456"));
        System.out.println("受影响行数："+i+"->添加成功");

        //提交事务
        sqlSession.commit();
            //关闭sqlSession
            sqlSession.close();
        }

    @Test
    public void updateUserTest(){
        // 1、 获得SqlSession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        //获得getMapper接口对象
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        int i = mapper.updateUser(new User(4, "呵呵", "159753"));
        System.out.println("受影响行数："+i+"->修改成功");

        //提交事务
        sqlSession.commit();
        //关闭sqlSession
        sqlSession.close();
    }


    @Test
    public void deleteUserTest(){
        // 1、 获得SqlSession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        //获得getMapper接口对象
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        int i = mapper.deleteUser(4);
        System.out.println("受影响行数："+i+"->删除成功");
        //提交事务
        sqlSession.commit();
        //关闭sqlSession
        sqlSession.close();
    }



}

