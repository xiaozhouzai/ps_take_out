package com.lcy.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lcy.reggie.pojo.Category;

public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
