package com.mybatiseasy.core;


import com.mybatiseasy.core.entity.User;
import com.mybatiseasy.core.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


@Slf4j
class OneTest{
    @Autowired
    UserMapper userMapper;

    @Test
    void test() {

        User user = new User();
        user.setId(11111L);
        user.setUsername("dudley");
        log.info("userMapper={}", userMapper);
        userMapper.insert(user);
    }

    @Test
    void validateMethod() throws Exception {

    }

}