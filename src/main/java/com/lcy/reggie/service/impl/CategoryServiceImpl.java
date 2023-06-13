package com.lcy.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcy.reggie.common.BusinessException;
import com.lcy.reggie.mapper.CategoryMapper;
import com.lcy.reggie.pojo.Category;
import com.lcy.reggie.pojo.Dish;
import com.lcy.reggie.pojo.Setmeal;
import com.lcy.reggie.service.CategoryService;
import com.lcy.reggie.service.DishService;
import com.lcy.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;



    @Override
    public void remove(Long id) {
        //构造条件查询控制器
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper=new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int countOne= (int) dishService.count(dishLambdaQueryWrapper);
        if(countOne>0){
            throw new BusinessException("关联数据，无法删除！");
        }
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper=new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int countTwo= (int) setmealService.count(setmealLambdaQueryWrapper);
        if(countTwo>0){
            throw new BusinessException("数据关联无法删除！");
        }

        super.removeById(id);

    }
}
