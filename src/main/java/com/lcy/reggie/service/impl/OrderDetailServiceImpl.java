package com.lcy.reggie.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcy.reggie.mapper.OrderDetailMapper;
import com.lcy.reggie.pojo.OrderDetail;
import com.lcy.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}