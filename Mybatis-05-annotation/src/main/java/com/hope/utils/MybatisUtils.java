package com.hope.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * 文件名：MybatisUtils
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/10/15-19:06
 * 描述：
 */
public class MybatisUtils {

    private static SqlSessionFactory sqlSessionFactory;

    static {
        String resource = "mybatis-config.xml";
        try {
            /**
             * 每个基于 MyBatis 的应用都是以一个 SqlSessionFactory 的实例为核心的。
             * SqlSessionFactory 的实例可以通过 SqlSessionFactoryBuilder 获得。
             * 而 SqlSessionFactoryBuilder 则可以从 XML 配置文件或一个预先配置的
             * Configuration 实例来构建出 SqlSessionFactory 实例。*/
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 既然有了 SqlSessionFactory，顾名思义，我们可以从中获得 SqlSession 的实例。
     * SqlSession 提供了在数据库执行 SQL 命令所需的所有方法。你可以通过 SqlSession
     * 实例来直接执行已映射的 SQL 语句。
     */
    public static SqlSession getSqlSession() {
//        工具类创建的时候实现自动提交事务!
        return sqlSessionFactory.openSession(true);
    }


}
