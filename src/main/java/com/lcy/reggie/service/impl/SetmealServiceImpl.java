package com.lcy.reggie.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcy.reggie.common.BusinessException;
import com.lcy.reggie.dto.SetmealDto;
import com.lcy.reggie.mapper.SetmealMapper;
import com.lcy.reggie.pojo.Setmeal;
import com.lcy.reggie.pojo.SetmealDish;
import com.lcy.reggie.service.SetmealDishService;
import com.lcy.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 保存套餐菜品信息
     * @param setmealDto
     */
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().peek((item) -> item.setSetmealId(setmealDto.getId())).collect(Collectors.toList());
        //保存套餐和菜品的关联信息，操作setmeal_dish,执行insert操作
        setmealDishService.saveBatch(setmealDishes);
    }

    @Transactional
    public void deleteWithDish(List<Long> ids) {
        //根据id删除setmeal信息
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        //判断是否可以删除
        int count= (int) this.count(queryWrapper);
        if(count>0){
            throw new BusinessException("套餐正在售卖，无法删除");
        }
        this.removeByIds(ids);
        //根据setmealId查询套餐菜品信息并删除
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(lambdaQueryWrapper);


    }


}
