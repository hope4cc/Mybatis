package com.hope.mapper;

import com.hope.pojo.Blog;

import java.util.List;
import java.util.Map;

/**
 * 文件名：BlogMapper
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/10/16-20:24
 * 描述：
 */
public interface BlogMapper {

    //插入数据
    int addBlog(Blog blog);

    //查询博客
    List<Blog> queryBlogIF(Map map);

    //查询条件博客
    List<Blog> queryBlogChoose(Map map);

    //更新博客
    int updateBlog(Map map);

    // 查询1-2-3-4号的记录
    List<Blog> queryBlogForeach(Map map);
}
