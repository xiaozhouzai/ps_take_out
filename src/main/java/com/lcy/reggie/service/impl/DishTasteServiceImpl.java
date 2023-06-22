package com.lcy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcy.reggie.mapper.DishTasteMapper;
import com.lcy.reggie.pojo.DishTaste;
import com.lcy.reggie.service.DishTasteService;
import org.springframework.stereotype.Service;

@Service
public class DishTasteServiceImpl extends ServiceImpl<DishTasteMapper, DishTaste> implements DishTasteService {
}
