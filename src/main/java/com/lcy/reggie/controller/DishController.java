package com.lcy.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcy.reggie.common.R;
import com.lcy.reggie.dto.DishDto;
import com.lcy.reggie.pojo.Category;
import com.lcy.reggie.pojo.Dish;
import com.lcy.reggie.pojo.DishTaste;
import com.lcy.reggie.service.CategoryService;
import com.lcy.reggie.service.DishService;
import com.lcy.reggie.service.DishTasteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 菜品管理
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishTasteService dishTasteService;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);

        return R.success("新增菜品成功");
    }

    /**
     * 菜品信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){

        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null,Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //执行分页查询
        dishService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){

        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return R.success(dishDto);
    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.updateWithFlavor(dishDto);

        return R.success("新增菜品成功");
    }

    /**
     * 根据id单个删除，并删除关联表中的dishtaste记录
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam Long[] ids){

        //根据dishId查询dishTaste对应的口味记录
        //创建条件控制器

        List<Long> dishTasteIds= new ArrayList<>();
        List<Long> dishIds= Arrays.stream(ids).toList();
        for (Long id : dishIds) {
            LambdaQueryWrapper<DishTaste> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(DishTaste::getDishId, id);
            List<DishTaste> dishTastes=dishTasteService.list(queryWrapper);
            for (DishTaste dishTaste : dishTastes) {
                Long dishTasteId =dishTaste.getId();
                dishTasteIds.add(dishTasteId);
            }
        }
        //先根据主键ids删除dishTaste记录
        dishTasteService.removeByIds(dishTasteIds);
        //再根据ids删除dish记录
        dishService.removeByIds(dishIds);
        return R.success("删除成功");
    }

    /**
     * 批量停售，批量起售
     * 请求方式POST
     * URL:http://localhost:8080/dish/status/0?ids=1666631373991002114,1666630908754608129
     */
    @PostMapping("/status/{statusId}")
    public R<String> updateStatus(@PathVariable int statusId,@RequestParam List<Long> ids){
        //根据idList批量查询dish信息
        for (Long dishId : ids) {
            Dish dish = dishService.getById(dishId);
            Integer status=dish.getStatus();
            if(status==0){
                if (statusId==0){
                    return R.error("已停售，操作失败");
                }
                if (statusId==1){
                    LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
                    queryWrapper.eq(Dish::getId,dishId);
                    dish.setStatus(1);
                    dishService.update(dish,queryWrapper);
                }

            }
            if(status==1){
                if (statusId==1){
                    return R.error("已起售，无需操作");
                }
                if (statusId==0){
                    LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
                    queryWrapper.eq(Dish::getId,dishId);
                    dish.setStatus(0);
                    dishService.update(dish,queryWrapper);
                }

            }
        }

        return R.success("操作成功");
    }


    /*@GetMapping("/list")
    public R<List<Dish>> list(Dish dish){
        LambdaQueryWrapper<Dish> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByDesc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        return R.success(list);


    }*/

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        log.info("dish:{}", dish);
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(dish.getName()), Dish::getName, dish.getName());
        queryWrapper.eq(null != dish.getCategoryId(), Dish::getCategoryId, dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        List<Dish> dishs = dishService.list(queryWrapper);

        List<DishDto> dishDtos = dishs.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Category category = categoryService.getById(item.getCategoryId());
            if (category != null) {
                dishDto.setCategoryName(category.getName());
            }
            LambdaQueryWrapper<DishTaste> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DishTaste::getDishId, item.getId());

            dishDto.setFlavors(dishTasteService.list(wrapper));
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtos);
    }


}
