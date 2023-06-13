package com.lcy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcy.reggie.common.R;
import com.lcy.reggie.pojo.Category;
import com.lcy.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 分类管理
 */
@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public R<Page<Category>> page(int page, int pageSize){
        //创建分页查询插件
        Page<Category> pageInfo =new Page<>(page,pageSize);
        //条件查询控制器
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        //创建条件
        queryWrapper.orderByDesc(Category::getSort);

        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    @PostMapping
    public R<String> save(@RequestBody Category category){
        //公共字段自动填充
        log.info("保存信息:{}",category);
        categoryService.save(category);
        return R.success("保存成功");
    }

    /**
     * 修改加保存，保存功能用save不需要重写
     * @param category
     * @return
     */
    @PutMapping
    public R<String> updateById(@RequestBody Category category){

        categoryService.updateById(category);

        return R.success("修改成功");
    }
    /**
     * 根据id删除分类
     */

    @DeleteMapping
    public R<String> delete(Long id){
        log.info("删除分类");
        categoryService.remove(id);
        return R.success("删除成功");
    }
    /**
     * 菜单页面展示菜品分类列表和套餐列表
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        //构造条件控制器
        LambdaQueryWrapper<Category>  queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        queryWrapper.orderByDesc(Category::getType).orderByDesc(Category::getUpdateTime);
        List<Category> list=categoryService.list(queryWrapper);
        return R.success(list);
    }


}
