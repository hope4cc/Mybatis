package com.hope.mapper;

import com.hope.pojo.Teacher;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 文件名：TeacherMapper
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/10/16-16:42
 * 描述：
 */
public interface TeacherMapper {

        //获取老师
        public List<Teacher> getTeacherList();

        //获取指定老师下的所有学生及老师的信息
        Teacher getTeacher(@Param("tid") int id);
        Teacher getTeacher2(@Param("tid") int id);

}
