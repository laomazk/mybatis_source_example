package com.magic.mb;

import com.magic.mb.bean.Page;
import com.magic.mb.entity.User;
import com.magic.mb.mapper.UserMapper;
import com.magic.mb.utils.ThreadLocalUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MbInterceptorApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testConn() {
        List<User> users = userMapper.selectList();
        users.forEach(System.out::println);
    }

    @Test
    void testUpdate() {
        User user = new User();
        user.setId(1);
        user.setUsername("张三");
        user.setAge(11);
        int a = userMapper.updateById(user);
        System.out.println("a = " + a);
    }

    @Test
    void testPageHelper() {
        Page page = new Page(1);
        ThreadLocalUtils.set(page);
        List<User> users = userMapper.selectList();
        users.forEach(System.out::println);
    }


}
