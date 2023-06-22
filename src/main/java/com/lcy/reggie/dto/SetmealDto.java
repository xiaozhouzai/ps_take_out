package com.lcy.reggie.dto;


import com.lcy.reggie.pojo.Setmeal;
import com.lcy.reggie.pojo.SetmealDish;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
