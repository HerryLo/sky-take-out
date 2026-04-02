package com.liuheng.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DishFlavor {
    // 主键
    private Long id;
    //'菜品ID'
    private Long dishId;
    // '口味名称（如：辣度）'
    private String name;
    // '口味选项（如：微辣,中辣,重辣）'
    private String value;

    //创建时间
    private LocalDateTime createTime;

    //更新时间
    private LocalDateTime updateTime;

    //创建人
    private Long createUser;

    //修改人
    private Long updateUser;
}
