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
    public void getByIdTest(){
        // 1、 获得SqlSession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try {
            //获得getMapper接口对象
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            User byId = mapper.getById(2);
            System.out.println(byId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            //关闭sqlSession
            sqlSession.close();
        }
    }

    @Test
    public void testLog4j(){
            logger.info("info:进入了testLog4j");
            logger.debug("debug:进入了testLog4j");
            logger.error("error:进入了testLog4j");
    }

    @Test
    public void getuserByLimit(){
        // 1、 获得SqlSession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try {
            //获得getMapper接口对象
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            HashMap<String, Object> map = new HashMap<>();
            map.put("startIndex",1);
            map.put("pageSize",5);
            List<User> userList = mapper.getuserByLimit(map);
            for (User user : userList) {
                System.out.println(user);
            }
            logger.info("info:查询成功");
        } catch (Exception e) {
            logger.error("error:参数错误");
            throw new RuntimeException(e);
        } finally {
            //关闭sqlSession
            sqlSession.close();
        }
    }

}

