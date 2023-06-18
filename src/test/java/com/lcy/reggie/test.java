package com.lcy.reggie;

import com.lcy.reggie.pojo.User;
import com.lcy.reggie.utils.RedisWords;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.lcy.reggie.utils.RedisWords.DISH_CATEGORY_ID;
/**
 * @author 李成阳
 * @date 2023/6/18
 * @Description
 */
@SpringBootTest
public class test {
    @Autowired
    private RedisTemplate<String,List<User>> redisTemplate;
    @Test
    void test01() {

        System.out.println("*"+DISH_CATEGORY_ID+"*");
    }


    @Test
    void test03() {
        List<User> list=new ArrayList<>();
        User user1=new User();
        User user2=new User();
        user1.setName("daiwd");
        user2.setName("lcy");
        user1.setPhone("+4646546");
        user2.setPhone("5164945");
        list.add(user1);
        list.add(user2);
        redisTemplate.opsForValue().set("user:123",list);
    }
}
