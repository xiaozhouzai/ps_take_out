package com.lcy.reggie.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcy.reggie.mapper.UserMapper;
import com.lcy.reggie.pojo.User;
import com.lcy.reggie.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
