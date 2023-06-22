package com.lcy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcy.reggie.mapper.ShoppingCartMapper;
import com.lcy.reggie.pojo.ShoppingCart;
import com.lcy.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @author 李成阳
 * @date 2023/6/14
 * @Description
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
