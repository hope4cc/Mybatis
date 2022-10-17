package com.hope.pojo;

import lombok.Data;

import java.util.Date;

/**
 * 文件名：Blog
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/10/16-20:20
 * 描述：
 */
@Data
public class Blog {
    private String id;
    private String title;
    private String author;
    private Date createTime; //属性名和字段名不一致
    private int views;
}
