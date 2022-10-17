package com.hope.mapper;

import com.hope.pojo.Teacher;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 文件名：TeacherMapper
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/10/16-16:42
 * 描述：
 */
public interface TeacherMapper {

    @Select("select * from teacher where id=#{id}")
    Teacher getTeacher(@Param("id") int id);
}
