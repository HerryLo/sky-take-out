package com.liuheng.dto;

import lombok.Data;

@Data
public class DishFlavorDTO {
    // '口味名称（如：辣度）'
    private String name;
    // '口味选项（如：微辣,中辣,重辣）'
    private String value;

    private Long dishId;

}
