环境：

- jdk1.8
- mysql5.7
- maven3.6.1
- idea

回顾：

- jdbc
- mysql
- java基础
- maven
- junit
# 1、简介
## 1.1、什么是MyBatis
- MyBatis 是一款优秀的**持久层框架**
- 它支持自定义 SQL、存储过程以及高级映射。
- MyBatis 免除了几乎所有的 JDBC 代码以及设置参数和获取结果集的工作。
- MyBatis 可以通过简单的 XML 或注解来配置和映射原始类型、接口和 Java POJO（Plain Old Java Objects，普通老式 Java 对象）为数据库中的记录。
- MyBatis本是apache的一个开源项目iBatis，2010年这个项目且改名为MyBatis。
- 2013年11月迁移到Github。

如何获得Mybatis

- maven仓库
```xml
<!-- https://mvnrepository.com/artifact/org.mybatis/mybatis -->
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.7</version>
</dependency>
```

- github：[https://github.com/mybatis/mybatis-3/releases](https://gitee.com/link?target=https%3A%2F%2Fgithub.com%2Fmybatis%2Fmybatis-3%2Freleases)
- 中文文档：[https://mybatis.org/mybatis-3/zh/index.html](https://gitee.com/link?target=https%3A%2F%2Fmybatis.org%2Fmybatis-3%2Fzh%2Findex.html)
## 1.2、持久化
数据持久化

- 持久化就是将程序的数据在持久状态和瞬时状态转化的过程
- **内存：断电即失**
- 数据库（jdbc），io文件持久化
- 生活：罐头

**为什么需要持久化？**

- 有一些对象，不能让它丢失
- 内存太贵
## 1.3、持久层
dao层 server层 controller层

- 完成持久化工作的代码块
- 层界限十分明显
## 1.4、为什么需要MyBatis

- 帮忙程序员将数据存入到数据库中
- 方便
- 传统的jdbc太复杂了。简化。框架。自动化。
- 不用mybatis也可以。容易上手。**技术没有高低之分**
- 优点：
   - 简单易学
   - 灵活
   - sql和代码的分离，提高了可维护性。
   - 提供映射标签，支持对象与数据库的orm字段关系映射
   - 提供对象关系映射标签，支持对象关系组建维护
   - 提供xml标签，支持编写动态sql
- **最重要的一点：使用的人多！**
# 第一个MyBatis程序
思路：搭建环境--》导入Mybatis--》编写代码--》测试
## 2.1、搭建环境
搭建数据库
```sql
CREATE DATABASE `mybatis`;

USE `mybatis`;

CREATE TABLE `user`(
  `id` INT(20) NOT NULL PRIMARY KEY,
  `name` VARCHAR(30) DEFAULT NULL,
  `pwd` VARCHAR(30) DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT INTO `user`(`id`,`name`,`pwd`) VALUES
(1,'zyy','123456'),
(2,'张三','123456'),
(3,'李四','123456');
```
新建项目

1. 新建一个普通的maven项目
2. 删除src目录（作为父工程）
3. 导入maven依赖
```xml
<dependencies>
  <dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.47</version>
  </dependency>

  <dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.7</version>
  </dependency>

  <dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
  </dependency>
</dependencies>
```
## 2.2、创建一个模板

- 编写mybatis的核心配置文件
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<!--configuration 核心配置文件-->
<configuration>
    <!--    environment 元素体中包含了事务管理和连接池的配置-->
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url"
                          value="jdbc:mysql://localhost:3306/mybatis?useSSL=false&amp;useUnicode=true&amp;characterEncoding=UTF-8"/>
                <property name="username" value="root"/>
                <property name="password" value="root"/>
            </dataSource>
        </environment>
    </environments>


    <!--    mappers 元素则包含了一组映射器（mapper），这些映射器的 XML 映射文件包含了 SQL 代码和映射定义信息。-->
    <!--   每一个Mapper.xml都需要在mybatis核心配置文件中注册 -->
    <mappers>
        <mapper resource="mapper/UserMapper.xml"/>
    </mappers>
</configuration>
```
编写mybatis工具类
```java
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
        return sqlSessionFactory.openSession();

    }


}

```
## 2.3、编写代码

- 实体类
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String name;
    private String pwd;
}
```

- Mapper接口
```java
public interface UserMapper {
    List<User> getAll();
}
```

- 接口实现类(由原来的UserDaoImpl转为一个Mapper配置文件)
```java
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- namespace 绑定一个对应的Dao接口 / mapper接口-->
<mapper namespace="com.hope.mapper.UserMapper">
    
<!--    select 查询语句 id 是 dao接口的 方法名-->
    <select id="getAll" resultType="com.hope.pojo.User">
        select * from user;
    </select>
        
</mapper>
```
junit测试
```java
public class UserMapperTest {
    @Test
    public void test(){
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
}
```
可能会遇到的问题

1. 配置文件没有注册
2. 绑定接口错误
3. 方法名不对
4. 返回类型不对
5. Maven导出资源问题

# 3、CRUD
增加(Create)、检索(Retrieve)、更新(Update)和删除(Delete)
## 1、namespace
namespace中的包名要和dao/mapper接口的包名一致
## 3、select
选择，查询语句：

- id ：就是对应的namespace中的方法名 也就是mapper接口中的方法
- resultType：sql语句执行的返回值
- parameterType：参数类型
1. 编写接口
```java
User getById(int id);
```

2. 编写接口对应的mapper中的语句
```xml
<select id="getById" resultType="com.hope.pojo.User" parameterType="int">
  select * from user where id=#{id}
</select>
```

3. 测试
```java
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
```
## 4、insert
编写接口
```java
 int  useradd(User user);
```
编写接口对应的mapper中的语句
```java
<!--    对象中的属性，可以直接取出来-->
    <insert id="useradd" parameterType="com.hope.pojo.User">
        insert into user (id,name,pwd) values (#{id},#{name},#{pwd})
    </insert>
```
测试
```java
    @Test
    public void useraddTest(){
        // 1、 获得SqlSession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();
            //获得getMapper接口对象
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        int i = mapper.useradd(new User(4, "菜菜", "123456"));
        System.out.println("受影响行数："+i+"->添加成功");

        //提交事务
        sqlSession.commit();
            //关闭sqlSession
            sqlSession.close();
        }
```
## 5、update
编写接口
```java
   int updateUser(User user);
```
编写接口对应的mapper中的语句
```java
   <update id="updateUser" parameterType="com.hope.pojo.User" >
        update user set name=#{name},pwd=#{pwd}  where id=#{id}
    </update>
```
测试
```java
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
```
## 6、delete
编写接口
```java
int deleteUser(int id);
```
编写接口对应的mapper中的语句
```java
  <delete id="deleteUser" parameterType="int">
        delete from user where id=#{id}
    </delete>
```
测试
```java
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
```
注意点：增删改需要提交事务才会生效
## 6、分析错误

- 标签不要匹配错！
- resource绑定mapper，需要使用路径！
- 程序配置文件必须符合规范！
- NullPointerException，没有注册到资源！
- 输出的xml文件中存在中文乱码问题！
- maven资源没有导出问题！
## 7、万能map
假设，我们的实例类，或者数据库中的表，字段或者参数过多，我们应该考虑使用map!
编写接口
```java
  int useradd2(Map<String,Object> map);
```
编写接口对应的mapper中的语句
```java
   <insert id="useradd2" parameterType="map">
        insert into user(id,name,pwd) values (#{userid},#{username},#{userpwd})
    </insert>
```
测试
```java
    @Test
    public void useradd2Test(){
        // 1、 获得SqlSession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        //获得getMapper接口对象
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        HashMap<String, Object> map = new HashMap<>();
        map.put("userid","5");
        map.put("username","苏苏");
        map.put("userpwd","222222");

        mapper.useradd2(map);
        System.out.println("添加成功");
        sqlSession.commit();

        //提交事务
        sqlSession.commit();
        //关闭sqlSession
        sqlSession.close();
    }
```
Map传递参数，直接在sql中取出key即可！
对象传递参数，直接在sql中取对象的属性即可！
只有一个基本类型的情况下，可以直接在sql中取到！
多个参数用Map，或者注解！![截屏2022-10-15 20.57.58.png](https://cdn.nlark.com/yuque/0/2022/png/29579322/1665838681587-392080b5-3587-40bd-885c-9c064c561e3a.png[a-z0-9\-&=%\.]*)

模糊查询怎么写？

1. java代码执行的时候，传递通配符%
```java
 /**
     * 模糊查询
     */
    @Test
    public void getuserlistTest(){
        // 1、 获得SqlSession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        //获得getMapper接口对象
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        List<User> userList = mapper.getuserlist("%菜%");
        for (User user : userList) {
            System.out.println(user);
        }
        //关闭sqlSession
        sqlSession.close();
    }
```
在sql中拼接使用通配符 （不推荐，容易引起sql注入问题）
```xml
<!--  select id, name, pwd from `user` where id = ?   -->
<!--  select id, name, pwd from `user` where id = 1      一般情况-->
<!--  select id, name, pwd from `user` where id = 1 or 1=1   sql注入情况-->
<select id="getUserLike" resultType="com.zyy.pojo.User" parameterType="string">
  select id, name, pwd from `user` where name like "%"#{value}"%"
</select>
```
# 4、配置解析
## 
1、核心配置文件

- mybatis-config.xml
- MyBatis 的配置文件包含了会深深影响 MyBatis 行为的设置和属性信息。 配置文档的顶层结构如下：
- configuration（配置）
   - [properties（属性）](https://mybatis.net.cn/configuration.html#properties)
   - [settings（设置）](https://mybatis.net.cn/configuration.html#settings)
   - [typeAliases（类型别名）](https://mybatis.net.cn/configuration.html#typeAliases)
   - [typeHandlers（类型处理器）](https://mybatis.net.cn/configuration.html#typeHandlers)
   - [objectFactory（对象工厂）](https://mybatis.net.cn/configuration.html#objectFactory)
   - [plugins（插件）](https://mybatis.net.cn/configuration.html#plugins)
   - [environments（环境配置）](https://mybatis.net.cn/configuration.html#environments)
      - environment（环境变量）
         - transactionManager（事务管理器）
         - dataSource（数据源）
   - [databaseIdProvider（数据库厂商标识）](https://mybatis.net.cn/configuration.html#databaseIdProvider)
   - [mappers（映射器）](https://mybatis.net.cn/configuration.html#mappers)
## 2、环境配置（environments）
MyBatis 可以配置成适应多种环境
**不过要记住：尽管可以配置多个环境，但每个 SqlSessionFactory 实例只能选择一种环境。**
学会使用配置多套运行环境！
MyBatis默认的事务管理器就是jdbc，连接池：POOLED
## 3、属性（properties）
我们可以通过properties属性来实现引用配置文件
这些属性可以在外部进行配置，并可以进行动态替换。你既可以在典型的 Java 属性文件中配置这些属性，也可以在 properties 元素的子元素中设置。【db.properties】
在核心配置文件中引入
```xml
  <properties resource="db.properties">         
		<property name="username" value="root"/>        
 		<property name="password" value="123456"/>    
 </properties>
```

- 可以直接引入外部文件
- 可以在其中增加一些属性配置
- **如果两个文件有同一个字段，优先使用外部配置文件的！**
## 4、类型别名（typeAliases）

- 类型别名可为 Java 类型设置一个缩写名字。
- 意在降低冗余的全限定类名书写。

核心配置文件
```xml
<!--    起别名-->
    <typeAliases>
        <typeAlias type="com.hope.pojo.User" alias="user" />
    </typeAliases>
```
```xml
  <select id="getAll" resultType="user">
        select * from user
    </select>
```
也可以指定一个包名，MyBatis 会在包名下面搜索需要的 Java Bean
扫描实体类的包，它的默认别名就是这个类的类名，首字母小写。
```xml
 <typeAliases>
        <package name="com.hope.pojo.User"/>
    </typeAliases>
```
在实体类比较少的时候，使用第一种方式。
如果实体类比较多，建议使用第二种。
第一种可以DIY别名，第二种不行，如果非要改的话，需要在实体上增加注解

```java
//在实体类 注解去别名
@Alias("user")
public class User {}
```
## 5、设置（settings）
[settings](https://mybatis.net.cn/configuration.html#settings)![截屏2022-10-15 21.45.49.png](https://cdn.nlark.com/yuque/0/2022/png/29579322/1665841552945-ae16b5eb-72fe-485c-927f-bf0798472b15.png)
![截屏2022-10-15 21.44.08.png](https://cdn.nlark.com/yuque/0/2022/png/29579322/1665841452371-5c873990-fea3-4c7d-bd2b-719f37850c42.png)
![截屏2022-10-15 21.45.22.png](https://cdn.nlark.com/yuque/0/2022/png/29579322/1665841528609-79f1a666-7f80-450a-83d3-b674b0d2b3e8.png)等等。。。。 🔗查看
## 
6、其他配置

- [typeHandlers（类型处理器）](https://gitee.com/link?target=https%3A%2F%2Fmybatis.org%2Fmybatis-3%2Fzh%2Fconfiguration.html%23typeHandlers)
- [objectFactory（对象工厂）](https://gitee.com/link?target=https%3A%2F%2Fmybatis.org%2Fmybatis-3%2Fzh%2Fconfiguration.html%23objectFactory)
- [plugins（插件）](https://gitee.com/link?target=https%3A%2F%2Fmybatis.org%2Fmybatis-3%2Fzh%2Fconfiguration.html%23plugins)
   - [mybatis-generator-core](https://gitee.com/link?target=https%3A%2F%2Fmvnrepository.com%2Fartifact%2Forg.mybatis.generator%2Fmybatis-generator-core)
   - [mybatis-plus](https://gitee.com/link?target=https%3A%2F%2Fmvnrepository.com%2Fartifact%2Fcom.baomidou%2Fmybatis-plus)
   - 通用mapper
## 7、映射器（mappers）
MapperRegistry：注册绑定我们的mapper文件
方式一：**推荐！！！！！！！！！！！！多数都用方式一**
```xml
<!--    mappers 元素则包含了一组映射器（mapper），
			  这些映射器的 XML 映射文件包含了 SQL 代码和映射定义信息。-->
<!--   每一个Mapper.xml都需要在mybatis核心配置文件中注册 -->
<mappers>
  <mapper resource="mapper/UserMapper.xml"/>
</mappers>
```
方式二：使用class文件绑定注册
```xml
<mappers>
    <mapper class="com.hope.mapper.UserMapper"/>
</mappers>
```
注意点：

- 接口和它的Mapper配置文件必须同名！
- 接口和它的Mapper配置文件必须在同一个包下！

方式三：使用扫描包进行注入绑定
```xml
    <mappers>
        <package name="com.hope.mapper"/>
    </mappers>
```
注意点：

- 接口和它的Mapper配置文件必须同名！
- 接口和它的Mapper配置文件必须在同一个包下！

## 8、生命周期和作用域![截屏2022-10-15 22.56.07.png](https://cdn.nlark.com/yuque/0/2022/png/29579322/1665845772117-959d1596-729f-434d-9b09-9b371ffc55d4.png)
生命周期和作用域类别是至关重要的，因为错误的使用会导致非常严重的**并发问题**。
#### SqlSessionFactoryBuilder

- 一旦创建了 SqlSessionFactory，就不再需要它了
- 局部变量
#### SqlSessionFactory

- 说白了就是可以想象为：数据库连接池
- 一旦被创建就应该在应用的运行期间一直存在，**没有任何理由丢弃它或重新创建另一个实例。**
- SqlSessionFactory 的最佳作用域是应用作用域
- 最简单的就是使用**单例模式**或者静态单例模式。

![image.png](https://cdn.nlark.com/yuque/0/2022/png/29579322/1665846028178-b6055013-a078-4004-a8f7-fa479f4feeb8.png)
这里的每一个mapper,就代表一个具体的业务！
#### SqlSession

- 连接到连接池的一个请求
- 每个线程都应该有它自己的 SqlSession 实例。SqlSession 的实例不是线程安全的，因此是不能被共享的，所以它的最佳的作用域是请求或方法作用域。
- 用完之后需要赶紧关闭，否则资源被占用！
# 5、解决属性名和字段名不一致的问题
## 1、问题
数据库中字段
![image.png](https://cdn.nlark.com/yuque/0/2022/png/29579322/1665846685846-0cdee919-7944-4c18-8826-b54c44bb6d96.png)
现在的实体类
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String name;
    private String password;
}
```
测试出现问题![截屏2022-10-15 23.14.11.png](https://cdn.nlark.com/yuque/0/2022/png/29579322/1665846857174-5ef55339-929b-46d6-80da-f6d926292b8d.png)解决方法：

- 起别名
```java
  <select id="getUserById" parameterType="int" resultType="com.zyy.pojo.User">
        select id, name, pwd as `password` from `user` where id = #{id} limit 1
    </select>
```

## 2、resultMap
结果集映射
:::tips
id  name  pwd 
id  name  password
:::
```java
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- namespace 绑定一个对应的Dao接口 / mapper接口-->
<mapper namespace="com.hope.mapper.UserMapper">


<!--   resultType sql语句执行的返回值-->
<!--   parameterType 参数类型-->

<!--    结果集映射-->
<!--    column 数据库的字段-->
<!--    property 实体类的属性-->
    <resultMap id="UserMap" type="User">
        <result column="id" property="id"></result>
        <result column="name" property="name"></result>
        <result column="pwd" property="password"></result>
    </resultMap>

    <select id="getById" resultType="user" parameterType="int">
        select * from user where id=#{id}
    </select>
    
</mapper>
```

- resultMap 元素是 MyBatis 中最重要最强大的元素
- ResultMap 的设计思想是，对简单的语句做到零配置，对于复杂一点的语句，只需要描述语句之间的关系就行了。
- ResultMap 的优秀之处——你完全可以不用显式地配置它们。
- 如果世界总是这么简单就好了。
# 6、日志
## 6.1、日志工厂
如果一个数据库操作 出现了异常，我们需要排错，日志就是最好的工具
曾经：sout、debug
现在：日志工厂
![image.png](https://cdn.nlark.com/yuque/0/2022/png/29579322/1665894394453-2fdb5f79-7b86-454e-9244-4effb29e523a.png)

- SLF4J
- LOG4J(deprecated since 3.5.9)
- LOG4J2
- JDK_LOGGING
- COMMONS_LOGGING
- STDOUT_LOGGING
- NO_LOGGING

在mybatis中具体使用哪一个日志实现，在设置中设定！
**STDOUT_LOGGING标准日志输出**
在mybatis核心配置文件中添加日志配置
```xml
<!--        标准的日志工厂实现-->
<settings>
  <setting name="logImpl" value="STDOUT_LOGGING"/>
</settings>
```
![截屏2022-10-16 12.31.52.png](https://cdn.nlark.com/yuque/0/2022/png/29579322/1665894715211-e338bad7-c850-41f5-888d-afeac348c825.png)
## 6.2、Log4J
什么是Log4J

- Log4j是[Apache](https://gitee.com/link?target=https%3A%2F%2Fbaike.baidu.com%2Fitem%2FApache%2F8512995)的一个开源项目，通过使用Log4j，我们可以控制日志信息输送的目的地是[控制台](https://gitee.com/link?target=https%3A%2F%2Fbaike.baidu.com%2Fitem%2F%25E6%258E%25A7%25E5%2588%25B6%25E5%258F%25B0%2F2438626)、文件、[GUI](https://gitee.com/link?target=https%3A%2F%2Fbaike.baidu.com%2Fitem%2FGUI)组件；
- 我们也可以控制每一条日志的输出格式；
- 通过定义每一条日志信息的级别，我们能够更加细致地控制日志的生成过程；
- 通过一个[配置文件](https://gitee.com/link?target=https%3A%2F%2Fbaike.baidu.com%2Fitem%2F%25E9%2585%258D%25E7%25BD%25AE%25E6%2596%2587%25E4%25BB%25B6%2F286550)来灵活地进行配置，而不需要修改应用的代码。
1. 导入log4j的包
```xml
<!-- https://mvnrepository.com/artifact/log4j/log4j -->
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>
```

2. log4j.properties
```xml
#将等级为DEBUG的日志输出到console和file这两个目的地，console和file的定义在下面配置中
log4j.rootLogger=DEBUG, console, file

#控制台输出的相关配置
log4j.appender.console=org.apache.log4j.ConsoleAppender
log4j.appender.console.Target = System.out
log4j.appender.console.Threshold=DEBUG
log4j.appender.console.layout=org.apache.log4j.PatternLayout
log4j.appender.console.layout.ConversionPattern=%d %p [%c] - %m%n

#文件输出的相关配置
log4j.appender.file=org.apache.log4j.RollingFileAppender
log4j.appender.file.File=./log/db.log
log4j.appender.file.MaxFileSize=10MB
log4j.appender.file.Threshold=DEBUG
log4j.appender.file.layout=org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern=%d %p [%c] - %m%n

#日志输出级别
log4j.logger.org.mybatis=DEBUG
log4j.logger.java.sql=DEBUG
log4j.logger.java.sql.Statement=DEBUG
log4j.logger.java.sql.ResultSet=DEBUG
log4j.logger.java.sql.PreparedStatement=DEBUG
```

3. 配置log4j为mybatis日志的实现
```xml
    <settings>
        <setting name="logImpl" value="LOG4J"/>
    </settings>
```

4. 使用log4j,运行测试![截屏2022-10-16 12.41.41.png](https://cdn.nlark.com/yuque/0/2022/png/29579322/1665895304189-3dccaae8-d02c-4deb-8d54-89cab68b7d99.png)

**简单使用**

1. 在要使用log4j的类中，导入包import org.apache.log4j.Logger;
2. 日志对象，参数为当前类的class
```java
//日志对象，参数为当前类的class
  static Logger logger = Logger.getLogger(UserMapperTest.class);

//日志级别
 @Test
    public void testLog4j(){
            logger.info("info:进入了testLog4j");
            logger.debug("debug:进入了testLog4j");
            logger.error("error:进入了testLog4j");
    }
```
# 7、分页
**思考：为啥要分页？**

- 减少数据的处理量
## 7.1、使用limit分页
:::tips
-- 语法 **select** * **from** `user` **limit** startIndex,pageSize;
select * from user limit 0,5;
:::
使用mybatis实现分页，核心sql

1. 接口
```java
  List<User> getuserByLimit(Map<String,Object> map);
```

2. Mapper.xml
```xml
<select id="getuserByLimit"  parameterType="map" resultMap="UserMap">
  select * from user limit #{startIndex},#{pageSize}
</select>
```

3. 测试
```java
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
```
## 7.2、分页插件
![image.png](https://cdn.nlark.com/yuque/0/2022/png/29579322/1665897510328-5f0e3f3b-6b56-4814-9c7c-5b9e0d9d7a07.png)官方文档：[https://pagehelper.github.io/](https://gitee.com/link?target=https%3A%2F%2Fpagehelper.github.io%2F)
# 8、使用注解开发
## 8.1、面向接口编程

1. 注解在接口上实现    
```java
@Select("select id, name, pwd from `user`")  
List<User> getUserList();
```

2. 需要在核心配置文件中绑定接口   
3. 测试    
```java
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
```
本质：反射机制
底层：动态代理

---

## 8.2、mybatis详细的执行流程
![image.png](https://cdn.nlark.com/yuque/0/2022/png/29579322/1665907126184-54dcc693-1433-44b8-8414-545f5be77eca.png)
## 8.3、CRUD
我们可以造工具类创建的时候实现自动提交事务！
```java
    public static SqlSession getSqlSession() {
//        工具类创建的时候实现自动提交事务!
        return sqlSessionFactory.openSession(true);
    }
```
编写接口，增加注解
```java
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

```
测试类
```java
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

```
【注意：我们必须要将接口注册绑定到我们的核心配置文件中】
```java
    <mappers>
        <mapper class="com.hope.mapper.UserMapper"/>
    </mappers>
```
**关于@Param()注解**

- 基本类型的参数或者String类型，需要加上
- 引用类型不需要加
- 如果只有一个基本类型的话，可以忽略，但是建议大家都加上
- 我们在sql中引用的就是我们这里的@Param()中设定的属性名

**#{} ${}区别**
别人博客：[https://blog.csdn.net/zymx14/article/details/78067452](https://gitee.com/link?target=https%3A%2F%2Fblog.csdn.net%2Fzymx14%2Farticle%2Fdetails%2F78067452)

# 9、Lombok
使用步骤

1. 在idea中安装lombok插件
2. 在项目中导入lombok的jar包
3. 在实体类上加注解即可
:::tips
   <!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->         <dependency>             
<groupId>org.projectlombok</groupId>            

 	<artifactId>lombok</artifactId>            
 	<version>1.18.22</version>         
</dependency>
:::
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String name;
    private String password;
}
```
注解
```java
@Getter and @Setter
@FieldNameConstants
@ToString
@EqualsAndHashCode
@AllArgsConstructor, @RequiredArgsConstructor and @NoArgsConstructor
@Log, @Log4j, @Log4j2, @Slf4j, @XSlf4j, @CommonsLog, @JBossLog, @Flogger, @CustomLog
@Data
@Builder
@SuperBuilder
@Singular
@Delegate
@Value
@Accessors
@Wither
@With
@SneakyThrows
@val
@var
```
实现原理：
[99%的程序员都在用Lombok，原理竟然这么简单？我也手撸了一个！|建议收藏！！！ - 云+社区 - 腾讯云 (tencent.com)](https://gitee.com/link?target=https%3A%2F%2Fcloud.tencent.com%2Fdeveloper%2Farticle%2F1608174)

# 10、多对一处理
多对一：

- 多个学生，对应一个老师
- 对于学生而言， **关联** ，多个学生，关联一个老师【多对一】
- 对于老师而言， **集合** ，一个老师有很多学生【一对多】![截屏2022-10-16 17.11.59.png](https://cdn.nlark.com/yuque/0/2022/png/29579322/1665911521768-18f9d5e0-8835-4dc6-8a86-091327516b23.png)
- SQL
```plsql
CREATE TABLE `teacher`
(
  `id`   INT(10) NOT NULL,
  `name` VARCHAR(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = INNODB
DEFAULT CHARSET = utf8;

INSERT INTO teacher(`id`, `name`)
VALUES (1, '秦老师');

CREATE TABLE `student`
(
  `id`   INT(10) NOT NULL,
  `name` VARCHAR(30) DEFAULT NULL,
  `tid`  INT(10)     DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fktid` (`tid`),
  CONSTRAINT `fktid` FOREIGN KEY (`tid`) REFERENCES `teacher` (`id`)
) ENGINE = INNODB
DEFAULT CHARSET = utf8;

INSERT INTO `student` (`id`, `name`, `tid`)
VALUES ('1', '小明', '1');
INSERT INTO `student` (`id`, `name`, `tid`)
VALUES ('2', '小红', '1');
INSERT INTO `student` (`id`, `name`, `tid`)
VALUES ('3', '小张', '1');
INSERT INTO `student` (`id`, `name`, `tid`)
VALUES ('4', '小李', '1');
INSERT INTO `student` (`id`, `name`, `tid`)
VALUES ('5', '小王', '1');
```
## 测试环境搭建

1. 导入lombok
2. 新建实体类Teacher，Student
3. 建立Mapper接口
4. 建立Mapper.xml文件
5. 在核心配置文件中绑定注册我们的Mapper接口或者文件【方式很多，自选】
6. 测试查询是否能够成功！![截屏2022-10-16 17.12.51.png](https://cdn.nlark.com/yuque/0/2022/png/29579322/1665911574728-e324f0b5-2bf9-4129-8849-becf5bab63f4.png)
7. Student.java
```java
@Data
    public class Student {
        private int id;
        private String name;
        private Teacher teacher;
    }
```

8. Teacher.java
```java
@Data
    public class Teacher {
        private int id;
        private String name;
    }
```
## 按照查询嵌套处理
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--configuration 核心配置文件-->
<mapper namespace="com.hope.mapper.StudentMapper">

  <!--
  思路：
  1、查询所有的学生
  2、根据查询出来的学生的tid,寻找对应的老师
  -->
  <resultMap id="StudentTeacher" type="Student">
    <id property="id" column="id"></id>
    <result property="name" column="name"></result>
    <association property="teacher" column="tid" javaType="Teacher" select="getTeacher"></association>
  </resultMap>

  <select id="getStudentList" resultMap="StudentTeacher">
    select * from student
  </select>

  <select id="getTeacher" resultType="Teacher">
    select * from teacher where id=#{id}
  </select>

</mapper>
```
## 按照结果嵌套处理
```xml

<!--    安装结果嵌套处理-->
    <resultMap id="StudentTeacher2" type="Student">
        <result property="id" column="id"></result>
        <result property="name" column="name"></result>
        <association property="teacher" javaType="Teacher">
            <result property="name" column="name"></result>
        </association>
     </resultMap>

    <select id="getStudent2" resultMap="StudentTeacher2">
        select s.id,s.name,t.name
        from student s,teacher t
        where s.tid=t.id;
    </select>

```
回顾mysql多对一查询方式：

- 子查询
- 链表查询
# 11、一对多处理
比如：一个老师拥有多个学生！
对于老师而言，就是一对多的关系！
## 环境搭建
实体类
```java
@Data
    public class Student {
        private int id;
        private String name;
        private int tid;
    }
```
```java
@Data
    public class Teacher {
        private int id;
        private String name;
        private List<Student> studentList;
    }
```
## 按照结果嵌套处理
```java
    <select id="getTeacherList" resultType="Teacher">
        select * from teacher
    </select>

    <!--
        复杂的属性，我们需要单独处理，对象：association 集合：collection
        javaType="" 指定属性的类型！
        集合中的泛型信息，我们使用ofType获取
-->
    <resultMap id="TeacherStudent" type="Teacher">
        <result property="id" column="tid"></result>
        <result property="name" column="tname"></result>
        <collection property="studentList" ofType="Student">
            <result property="id" column="sid"></result>
            <result property="name" column="sname"></result>
            <result property="tid" column="tid"></result>
        </collection>
    </resultMap>
    
    <select id="getTeacher" resultMap="TeacherStudent" parameterType="int">
        select s.id sid,s.name sname,t.name tname,t.id tid
        from student s,teacher t
        where s.tid=t.id and t.id=#{tid}
    </select>
```
## 按照查询嵌套处理
```java
   <select id="getTeacher2" resultMap="TeacherStudent2">
        select * from teacher where id=#{tid}
    </select>

    <resultMap id="TeacherStudent2" type="Teacher">
        <collection property="studentList" javaType="ArrayList" ofType="Student" select="getStudentByTeacherId" column="id"></collection>
    </resultMap>

    <select id="getStudentByTeacherId" resultType="Student">
        select * from student where tid=#{tid}
    </select>
```
## 小结

1. 关联 - association【多对一】
2. 集合 - collection【一对多】
3. javaType & ofType
   - javaType 用来指定实体类中属性的类型
   - ofType 用来指定映射到List或者集合中pojo类型，泛型中的约束类型。
## 注意点

- 保证sql的可读性，尽量保证通俗易懂
- 注意一对多和多对一中，属性和字段的问题
- 如果问题不好排查错误，可以使用日志，建议使用Log4j

面试高频：

- mysql引擎
- innodb底层原理
- 索引
- 索引优化

# 12、动态SQL
**什么是动态sql：动态sql就是根据不同的条件生成不同的sql语句。**
如果你之前用过 JSTL 或任何基于类 XML 语言的文本处理器，你对动态 SQL 元素可能会感觉似曾相识。在 MyBatis 之前的版本中，需要花时间了解大量的元素。借助功能强大的基于 OGNL 的表达式，MyBatis 3 替换了之前的大部分元素，大大精简了元素种类，现在要学习的元素种类比原来的一半还要少。

- if
- choose (when, otherwise)
- trim (where, set)
- foreach
## 搭建环境
```java
CREATE TABLE `blog`
(
    `id`          VARCHAR(50)  NOT NULL COMMENT '博客id',
    `title`       VARCHAR(100) NOT NULL COMMENT '博客标题',
    `author`      VARCHAR(30)  NOT NULL COMMENT '博客作者',
    `create_time` DATETIME     NOT NULL COMMENT '创建时间',
    `views`       INT(30)      NOT NULL COMMENT '浏览量'
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;
```
创建一个基础工程

1. 导包
2. 编写配置文件
3. 编写实体类
```java
@Data
public class Blog {
    private String id;
    private String title;
    private String author;
    private Date createTime; //属性名和字段名不一致
    private int views;
}
```

4. 编写实体类对应Mapper接口和Mapper.xml文件

## if
```xml
<select id="queryBlogIF" resultType="blog">
  select *
  from blog
  where  1=1
  <if test="title!=null">
    and   title=#{title}
  </if>
  <if test="author!=null">
    and   author=#{author}
  </if>
</select>

//where元素只会在至少有一个子元素的条件返回SQL子句的情况下才去插入“VHERE"子句。
//而且，若语句的开头为“AND”或“OR”,，where元素也会将它们去除。
   <select id="queryBlogIF" parameterType="map" resultType="blog">
        select *
        from blog
        <where> 
            <if test="title!=null">
             and   title=#{title}
            </if>
        <if test="author!=null">
            and   author=#{author}
        </if>
        </where>
    </select>
```
## choose (when, otherwise)
```xml
  <select id="queryBlogChoose" parameterType="map" resultType="blog">
        select * from blog
        <where>
            <choose>
                <when test="title!=null">
                    title=#{title}
                </when>
                <when test="author!=null">
                    and author=#{author}
                </when>
                <otherwise>
                    and views=#{views}
                </otherwise>
            </choose>
        </where>
    </select>

```
## trim (where, set)
_where_ 元素只会在子元素返回任何内容的情况下才插入 “WHERE” 子句。而且，若子句的开头为 “AND” 或 “OR”，_where_ 元素也会将它们去除。
```xml
  <select id="queryBlogChoose" parameterType="map" resultType="blog">
        select * from blog
        <where>
            <choose>
                <when test="title!=null">
                    title=#{title}
                </when>
                <when test="author!=null">
                    and author=#{author}
                </when>
                <otherwise>
                    and views=#{views}
                </otherwise>
            </choose>
        </where>
    </select>
```
```xml
 <update id="updateBlog" parameterType="map">
        update blog
        <set>
            <if test="title!=null">
                title=#{title},
            </if>
            <if test="author!=null">
                author=#{author}
            </if>
        </set>
        where id=#{id}
    </update>
```
如果 _where_ 元素与你期望的不太一样，你也可以通过自定义 trim 元素来定制 _where_ 元素的功能。比如，和 _where_ 元素等价的自定义 trim 元素为：
:::tips
<trim prefix="WHERE" prefixOverrides="AND |OR ">  
 ... 
</trim>
:::
:::tips
<trim prefix="SET" suffixOverrides=","> 
  ... 
</trim>
:::
**所谓的动态sql。本质还是sql语句，只是我们可以在sql层面，去执行一个逻辑代码**
## SQL片段
有的时候，我们可能会将一些功能的部分抽取出来，方便复用

1. 使用sql标签抽取公共的部分
```xml
<!-- 使用sql标签抽取公共的部分 -->
<sql id="selectlog">
        select * from blog
 </sql>

<!-- IF -->
  <select id="queryBlogIF" parameterType="map" resultType="blog">
<!--     在需要使用的地方使用include标签引用即可 -->
     <include refid="selectlog"></include>
    
        <where>
            <if test="title!=null">
                and title=#{title}
            </if>
            <if test="author!=null">
                and author=#{author}
            </if>
        </where>
    </select>


<!-- Choose -->
    <select id="queryBlogChoose" parameterType="map" resultType="blog">
<!--       在需要使用的地方使用include标签引用即可 -->
        <include refid="selectlog"></include>
      
        <where> 
            <choose>
                <when test="title!=null">
                    title=#{title}
                </when>
                <when test="author!=null">
                    and author=#{author}
                </when>
                <otherwise>
                    and views=#{views}
                </otherwise>
            </choose>
        </where>
    </select>
```
注意事项：

- 最好基于单表来定义sql片段
- 不要存在where标签
## foreach
修改id 为1，2，3 方便测试
![截屏2022-10-16 22.42.21.png](https://cdn.nlark.com/yuque/0/2022/png/29579322/1665931345597-821f55dd-33f1-4056-989b-fadfed4ed41c.png)
```xml
<!--现在传递一个万能map，这个map中存在一个集合-->
 <!--
        select * from blog 
				where 1=1 and (id ='1' or id='2'or id='3')
 -->
    <select id="queryBlogForeach" resultType="blog">
        select * from blog
        <where>
           <foreach collection="ids" item="id" open="and (" close=")" separator="or">
               id=#{id}
           </foreach>
        </where>
    </select>
```
测试类
```xml

    @Test
    public void queryBlogForeach(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        HashMap<Object, Object> map = new HashMap<>();

        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);
        map.put("ids",ids);
        List<Blog> blogs = mapper.queryBlogForeach(map);
        for (Blog blog : blogs) {
            System.out.println(blog);
        }

        sqlSession.close();
    }

```
结果
![截屏2022-10-16 22.44.09.png](https://cdn.nlark.com/yuque/0/2022/png/29579322/1665931452144-e2b4ef82-6563-48e1-94ad-8844b40d2ded.png)

---

动态sql就是在拼接sql语句，我们只要保证sql正确性，按照sql的格式，去排列组合就可以了

# 13、缓存
## 13.1、简介

1. 什么是缓存[cache]
   - 存在内存中的临时数据
   - 将用户经常查询的数据放在缓存（内存）中，用户去查询数据就不用从磁盘上（关系型数据库数据文件）查询，从缓存中查询，从而提高查询效率，解决了高并发系统的性能问题。
2. 为什么使用缓存
   - 减少和数据库的交互次数，减少系统开销，提高系统效率
3. 什么样的数据能使用缓存
   - 经常查询并且不经常改变的数据
## 13.2、mybatis缓存

- mybatis包含一个非常强大的查询缓存特性，它可以非常方便地定制和配置缓存。缓存可以极大的提高查询效率。
- mybatis系统中默认定义了两级缓存：**一级缓存**和**二级缓存**
   - 默认情况下，只有一级缓存开启。（sqlsession级别的缓存，也称为本地缓存）
   - 二级缓存需要手动开启和配置，它是基于namespace级别的缓存。
   - 为了提高扩展性，mybatis定义了缓存接口cache。我们可以通过实现cache接口来自定义二级缓存。
## 13.3、一级缓存

- 一级缓存也叫本次缓存
   - 与数据库同一次会话期间查询到的数据会放到本次缓存中。
   - 以后如果需要获取相同的数据，直接从缓存中拿，没必须再去查询数据库
1. 开启日志
2. 测试再一个session中查询两次相同的记录
```java
@Test
    public void getTeacherList(){
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);

    List<User> user1 = mapper.quertUserById(1);
    System.out.println(user1);

    //        mapper.updateUser(new User(2,"hhahha","123456"));

    //手动清理缓存 
    sqlSession.clearCache();

    System.out.println("==========================");
    List<User> user2 = mapper.quertUserById(1);
    System.out.println(user2);

    sqlSession.close();
}
```
查询日志输出![截屏2022-10-17 16.09.07.png](https://cdn.nlark.com/yuque/0/2022/png/29579322/1665994149909-a42c87a0-9a35-4f16-897f-8f30a7249ab5.png)级缓存默认是开启的，只在一次SqlSession中有效，也就是拿到连接到关闭连接这个区间段！

缓存失效的情况：
查询条件不同
![截屏2022-10-17 16.10.57.png](https://cdn.nlark.com/yuque/0/2022/png/29579322/1665994259993-52171ed0-e56e-4cb4-832c-029d43649419.png)
增删改操作，可能会改变原来的数据，所以必定会刷新缓存
![截屏2022-10-17 16.11.37.png](https://cdn.nlark.com/yuque/0/2022/png/29579322/1665994299516-3dc6d82a-005b-47e8-8507-23835e2f9856.png)
查询不同的Mapper.xml
手动清理缓存
:::tips
 //手动清理缓存             sqlSession.clearCache();
:::
查询的同条件，但是还是执行了两次
![截屏2022-10-17 16.12.38.png](https://cdn.nlark.com/yuque/0/2022/png/29579322/1665994361086-479e94bd-ded2-4514-b24e-5a6f48a5378a.png)小结：

- 一级缓存默认是开启的，只在一次sqlsession中有效，也就是拿到连接到关闭了连接这个区间段！
- 一级缓存就是一个map
## 
13.4、二级缓存

- 二级缓存也叫全局缓存，一级缓存作用域太低了，所以诞生了二级缓存。
- 基本namespace级别的缓存，一个名称空间，对应一个二级缓存
- 工作机制
   - 一个会话查询一个数据，这个数据就会被放到当前会话的一级缓存中
   - 如果当前会话关闭了，这个会话对应的一级缓存就没了，但是我们想要的是，会话关闭了，一级缓存中的数据被保存到二级缓存中
   - 新的会话查询信息，就可以直接从二级缓存中获取内容
   - 不同的mapper查出的数据会放在自己对应的缓存（map）中

步骤

1. 开启全部缓存

mybatis-config.xml
```java
    <settings>
        <setting name="logImpl" value="STDOUT_LOGGING"/>
        <!--        显式开启全局缓存-->
        <setting name="cacheEnabled" value="true"/>
    </settings>
```

2. 在要使用二级缓存的mapper中开启
```java
 <!--    在当前mapper.xml中使用二级缓存-->
    <cache/>


      <!--    在当前mapper.xml中使用二级缓存-->
    <cache
            eviction="FIFO"
            flushInterval="60000"
            size="512"
            readOnly="true"/>
```

3. 测试
```java
  @Test
    public void getTeacherList(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> user1 = mapper.quertUserById(3);
        System.out.println(user1);
        sqlSession.close();


        System.out.println("==========================");

        SqlSession sqlSession2 = MybatisUtils.getSqlSession();
        UserMapper mapper2 = sqlSession2.getMapper(UserMapper.class);
        List<User> user2 = mapper2.quertUserById(3);
        System.out.println(user2);

        System.out.println(user1==user2);


        sqlSession2.close();
        
    }
```
问题：我们需要将实体类序列化，否则就会报错
```java

/**
 * 文件名：User
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/10/17-15:15
 * 描述：mybatis 开启了序列化,存入缓存需要序列化
 *      需要    implements Serializable
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User implements Serializable {
    private int id;
    private String name;
    private String pwd;
}

```
小结：

- 只要开启了二级缓存，在同一个Mapper下就有效
- 所有的数据都会先放在一级缓存中
- **只有当会话提交或者关闭的时候，才会提交到二级缓存中**。
## 13.5、缓存原理

1. 先查询的二级缓存，查到直接返回
2. 查不到，再查一级缓存，查到直接返回
3. 查不到，查数据库
```java
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
```
![image.png](https://cdn.nlark.com/yuque/0/2022/png/29579322/1665997503582-6b946b9d-e730-4c8a-b324-378736db8604.png)
## 13.6、自定义缓存-ehcache
要在程序中使用ehcache，先导包
```java
<!--        mybatis ehcache 缓存 -->
        <dependency>
            <groupId>org.mybatis.caches</groupId>
            <artifactId>mybatis-ehcache</artifactId>
            <version>1.1.0</version>
        </dependency>
```
在mapper中指定使用我们的ehcache缓存实现
```java
    <cache type="org.mybatis.caches.ehcache.EhcacheCache"/>
```
配置文件ehcache.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="http://ehcache.org/ehcache.xsd"
  updateCheck="false">
  <!--
  diskStore：为缓存路径，ehcache分为内存和磁盘两级，此属性定义磁盘的缓存位置。参数解释如下：
  user.home – 用户主目录
  user.dir – 用户当前工作目录
  java.io.tmpdir – 默认临时文件路径
  -->
  <diskStore path="./tmpdir/Tmp_EhCache"/>

  <defaultCache
    eternal="false"
    maxElementsInMemory="10000"
    overflowToDisk="false"
    diskPersistent="false"
    timeToIdleSeconds="1800"
    timeToLiveSeconds="259200"
    memoryStoreEvictionPolicy="LRU"/>

  <cache
    name="cloud_user"
    eternal="false"
    maxElementsInMemory="5000"
    overflowToDisk="false"
    diskPersistent="false"
    timeToIdleSeconds="1800"
    timeToLiveSeconds="1800"
    memoryStoreEvictionPolicy="LRU"/>
  <!--
  defaultCache：默认缓存策略，当ehcache找不到定义的缓存时，则使用这个缓存策略。只能定义一个。
  -->
  <!--
  name:缓存名称。
  maxElementsInMemory:缓存最大数目
  maxElementsOnDisk：硬盘最大缓存个数。
  eternal:对象是否永久有效，一但设置了，timeout将不起作用。
  overflowToDisk:是否保存到磁盘，当系统当机时
  timeToIdleSeconds:设置对象在失效前的允许闲置时间（单位：秒）。仅当eternal=false对象不是永久有效时使用，可选属性，默认值是0，也就是可闲置时间无穷大。
  timeToLiveSeconds:设置对象在失效前允许存活时间（单位：秒）。最大时间介于创建时间和失效时间之间。仅当eternal=false对象不是永久有效时使用，默认是0.，也就是对象存活时间无穷大。
  diskPersistent：是否缓存虚拟机重启期数据 Whether the disk store persists between restarts of the Virtual Machine. The default value is false.
  diskSpoolBufferSizeMB：这个参数设置DiskStore（磁盘缓存）的缓存区大小。默认是30MB。每个Cache都应该有自己的一个缓冲区。
  diskExpiryThreadIntervalSeconds：磁盘失效线程运行时间间隔，默认是120秒。
  memoryStoreEvictionPolicy：当达到maxElementsInMemory限制时，Ehcache将会根据指定的策略去清理内存。默认策略是LRU（最近最少使用）。你可以设置为FIFO（先进先出）或是LFU（较少使用）。
  clearOnFlush：内存数量最大时是否清除。
  memoryStoreEvictionPolicy:可选策略有：LRU（最近最少使用，默认策略）、FIFO（先进先出）、LFU（最少访问次数）。
  FIFO，first in first out，这个是大家最熟的，先进先出。
  LFU， Less Frequently Used，就是上面例子中使用的策略，直白一点就是讲一直以来最少被使用的。如上面所讲，缓存的元素有一个hit属性，hit值最小的将会被清出缓存。
  LRU，Least Recently Used，最近最少使用的，缓存的元素有一个时间戳，当缓存容量满了，而又需要腾出地方来缓存新的元素的时候，那么现有缓存元素中时间戳离当前时间最远的元素将被清出缓存。
  -->

</ehcache>
```
