package com.hope.pojo;

import lombok.Data;

/**
 * 文件名：Student
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/10/16-16:40
 * 描述：多对一
 */
@Data
public class Student {
    private int id;
    private String name;
    //学生需要关猴一个老师！
    private Teacher teacher;
}