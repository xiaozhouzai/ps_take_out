package com.lcy.reggie.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lcy.reggie.dto.SetmealDto;
import com.lcy.reggie.pojo.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);

    public void deleteWithDish(List<Long> ids);
}
