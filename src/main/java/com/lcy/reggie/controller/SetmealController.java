package com.lcy.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcy.reggie.common.R;
import com.lcy.reggie.dto.SetmealDto;
import com.lcy.reggie.pojo.Category;
import com.lcy.reggie.pojo.Dish;
import com.lcy.reggie.pojo.Setmeal;
import com.lcy.reggie.service.CategoryService;

import com.lcy.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;
    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page<SetmealDto>> page(int page,int pageSize,String name){
        Page<Setmeal> pageInfo=new Page<>(page,pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Setmeal::getName,name);
        setmealService.page(pageInfo, queryWrapper);
        Page<SetmealDto> dtoPage=new Page<>();
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> setmealDtos = records.stream().map(item -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }

            return setmealDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(setmealDtos);
        return R.success(dtoPage);
        /*//分页构造器对象
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据name进行like模糊查询
        queryWrapper.like(name != null,Setmeal::getName,name);
        //添加排序条件，根据更新时间降序排列
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            //对象拷贝
            BeanUtils.copyProperties(item,setmealDto);
            //分类id
            Long categoryId = item.getCategoryId();
            //根据分类id查询分类对象
            Category category = categoryService.getById(categoryId);
            if(category != null){
                //分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);
        return R.success(dtoPage);*/
    }
    @PostMapping("/status/{statusId}")
    public R<String> updateStatus(@PathVariable int statusId,@RequestParam List<Long> ids){
        //如果statusId为0，说明停售状态
        for (Long id : ids) {
            Setmeal setmeal = setmealService.getById(id);
            Integer status = setmeal.getStatus();
            if(status==0){
                if (statusId==0){
                    return R.error("已停售,无需操作");
                }
                if (statusId==1){
                    LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
                    queryWrapper.eq(Setmeal::getId,id);
                    setmeal.setStatus(1);
                    setmealService.update(setmeal,queryWrapper);
                }
            }
            if(status==1){
                if (statusId==1){
                    return R.error("已起售，无需操作");
                }
                if (statusId==0){
                    LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
                    queryWrapper.eq(Setmeal::getId,id);
                    setmeal.setStatus(0);
                    setmealService.update(setmeal,queryWrapper);
                }
            }
        }
        return R.success("修改状态成功");
    }

    /**
     * 保存套餐信息
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("保存套餐:{}",setmealDto.getCategoryId());
        setmealService.saveWithDish(setmealDto);
        return R.success("保存成功");
    }

    /**
     * 根据前端传过来的setmeal_id删除对应关联表信息
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> remove(@RequestParam List<Long> ids){
        log.info("需要删除的id:{}",ids);
        setmealService.deleteWithDish(ids);
        return R.success("删除成功");
    }
}
