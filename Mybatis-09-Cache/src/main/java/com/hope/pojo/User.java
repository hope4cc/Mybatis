package com.hope.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
