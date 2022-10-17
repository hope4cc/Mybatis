package com.hope;


import com.hope.mapper.UserMapper;
import com.hope.pojo.User;
import com.hope.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;


import java.util.List;

/**
 * 文件名：MyTest
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/10/16-16:48
 * 描述：
 */
public class MyTest {

    @Test
    public void getTeacherList(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> user1 = mapper.quertUserById(1);
        System.out.println(user1);
        sqlSession.close();


        System.out.println("==========================");

        SqlSession sqlSession2 = MybatisUtils.getSqlSession();
        UserMapper mapper2 = sqlSession2.getMapper(UserMapper.class);
        List<User> user2 = mapper2.quertUserById(1);
        System.out.println(user2);



        List<User> user3 = mapper2.quertUserById(2);
        System.out.println(user3);
        List<User> user4 = mapper2.quertUserById(2);
        System.out.println(user4);

        sqlSession2.close();

    }
}
