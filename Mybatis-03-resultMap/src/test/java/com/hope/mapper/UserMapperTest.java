package com.hope.mapper;

import com.hope.pojo.User;
import com.hope.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;



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
}

