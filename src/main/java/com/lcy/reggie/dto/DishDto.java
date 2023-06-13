package com.lcy.reggie.dto;

import com.lcy.reggie.pojo.Dish;
import com.lcy.reggie.pojo.DishTaste;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;


/**
 * DTO:,数据传输对象，用于展示层与服务层之间的数据传输
 * 业务拓展对象
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class DishDto extends Dish {

    private List<DishTaste> flavors = new ArrayList<>();  //口味列表

    private String categoryName;  //分类名

    private Integer copies;  //副本
}
