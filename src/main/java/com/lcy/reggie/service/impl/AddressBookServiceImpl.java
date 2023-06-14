package com.lcy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcy.reggie.mapper.AddressBookMapper;
import com.lcy.reggie.pojo.AddressBook;
import com.lcy.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author 李成阳
 * @date 2023/6/14
 * @Description
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
