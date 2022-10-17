package com.hope.pojo;

import lombok.Data;

import java.util.List;

/**
 * 文件名：Teacher
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/10/16-16:40
 * 描述：一对多处理
 */
@Data
public class Teacher {
    private int id;
    private String name;
    private List<Student> studentList;
}