package com.lcy.reggie.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lcy.reggie.pojo.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
